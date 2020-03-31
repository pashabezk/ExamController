import TableLists.ExamTableList;
import TableLists.ExpellTableList;
import TableLists.MarkTableList;
import TableLists.RatingGroupTableList;
import TableLists.RatingStudentTableList;
import TableLists.StipendTableList;
import TableLists.StudentTableList;
import TableLists.UsersTableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseHandler
{
    public static String DBURL = ""; //URL сервера
    public static String DBPORT = ""; //порт сервера
    public static String DBNAME = ""; //название БД
    public static String MYSQL_USER = ""; //пользователь MySQL
    public static String MYSQL_PASSWORD = ""; //пароль пользователя MySQL
    private static Connection DBConnection = null;

    public static Connection getDBConnection() throws SQLException
    {
        if (DBConnection == null) //проверка не открыт ли уже доступ к БД
        {
            Properties p=new Properties(); //создание параметров для подключения к БД
            p.setProperty("user", MYSQL_USER);
            p.setProperty("password", MYSQL_PASSWORD);
            p.setProperty("useUnicode","true");
            p.setProperty("characterEncoding","cp1251");
            DBConnection = DriverManager.getConnection("jdbc:mysql://" + DBURL + ":" + DBPORT + "/" + DBNAME,p); //создание подключения к БД
        }
        return DBConnection;
    }

    public static void closeDB() throws SQLException
    {
        DBConnection.close();
        DBConnection = null;
    }

    public static int createDatabase(String dbName) //создание БД
    {
        int ret = -1;
        //-1 - не удалось подключиться к БД
        // 0 - не удалось создать БД
        // 1 - удачное создание БД

        String statements[] = new String[]
                {
                    "create database " + dbName + " default charset cp1251;",
                    "use " + dbName + ";",
                    "create table groupp (id int primary key auto_increment, name char(5), course int, year int) engine=MyISAM;",
                    "create table student (num int primary key auto_increment, surname char(64), name varchar(64), patronymic char(64), gr int, status int, phone char(11), mail char(64), foreign key(gr) references groupp(id)) engine=MyISAM;",
                    "create table exam (id int primary key auto_increment, name char(64), mark_st int, id_t int, gr int, ddate date, foreign key(id_t) references user(id), foreign key(gr) references groupp(id)) engine=MyISAM;",
                    "create table user (id int primary key auto_increment, login char(32), pswd char(32), type int, surname char(64), name char(64), patronymic char(64), phone char(11), mail char(64)) engine=MyISAM;",
                    "create table mark (id int primary key auto_increment, exam int, num_rb int, id_t int, mark int, ddate date, retake int, foreign key(exam) references exam(id), foreign key(num_rb) references student(num), foreign key(id_t) references user(id)) engine=MyISAM;",
                    "insert into user values (default, 'admin', 'admin', 3, '', '', '', '', '');"
                };

        try
        {
            Connection connection = getDBConnection(); //получение подключения к БД
            try {
                for (String st: statements)
                    connection.createStatement().executeUpdate(st); //выполнение запросов на создание БД
                closeDB();
                ret = 1;
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                ret = 0; //установка ret в ноль - не удалось создать БД
                connection.createStatement().executeUpdate("drop database if exists " + dbName + ";"); //попытка удалить недосозданную БД
            }
        } catch(SQLException e) {e.printStackTrace();}
        return ret;
    }

    public static int signIn(String login, String password)
    {

        int ret = -1;
        //-1 - не удалось подключиться к БД
        // 0 - пользователь не найден (неправильный логин/пароль)
        //в случае успешного подключения взвращается идентификатор пользователя
        try {
            Statement st = getDBConnection().createStatement();
            ResultSet result = st.executeQuery("select count(id) from user where login=\'"
                    + login + "\' and pswd = \'" + password + "\'"); //выполнение запроса на авторизацию

            if((result.next())&&(result.getInt(1)==1)) //если была получен всего один пользователь, значит правильный логин и пароль
            {
                Statement st2 = getDBConnection().createStatement();
                ResultSet result2 = st2.executeQuery("select id from user where login=\'"
                        + login + "\' and pswd = \'" + password + "\'"); //получение идентификатора пользователя
                if(result2.next())
                    ret = result2.getInt(1);
            }
            else ret = 0;
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return ret;
    }

    public static int checkPassword(int id, String password)
    {
        int ret = -1;
        //-1 - не удалось подключиться к БД
        // 0 - неправильный пароль
        // 1 - правильный пароль
        try {
            Statement st = getDBConnection().createStatement();
            ResultSet result = st.executeQuery("select count(id) from user where id="
                    + id + " and pswd = \'" + password + "\'"); //если возвращается ноль, значит у пользователя с таким id другой пароль, иначе вернется единица

            if((result.next())&&(result.getInt(1)==1)) //если была получен всего один пользователь, значит правильный логин и пароль
                ret = 1;
            else ret = 0;
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return ret;
    }

    public static ArrayList<UsersTableList> getUsers() //получение списка всех пользователей
    {
        ArrayList<UsersTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select id, type, name, surname, patronymic, phone, mail from user;"); //получение информации о пользователях
            while(result.next()) {
                al.add(new UsersTableList(result.getInt("id"), result.getInt("type"),
                        result.getString("name"), result.getString("surname"),
                        result.getString("patronymic"), result.getString("phone"), result.getString("mail")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static User getUserByID(int id) //получение информации о пользователе по идентификатору
    {
        User user = new User();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select id, type, name, surname, patronymic, login, phone, mail from user where id=" + id + ";"); //получение информации о пользователе
            if(result.next()) //если пользователь получен
                user = new User(result.getInt("id"), result.getInt("type"),
                        result.getString("name"), result.getString("surname"),
                        result.getString("patronymic"), result.getString("login"),
                        result.getString("phone"), result.getString("mail"));
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return user;
    }

    public static int isLoginExists(String login) //проверка, существует ли такой логин в системе
    {
        int isExists = -1; //в случае, если не удастся подключиться к БД, будет возвращена -1
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select count(login) from user where login = '" + login + "';"); //получение количества заданных логинов в системе
            if(result.next()) //если login существует, вернется 1, если не найден - 0
                isExists = result.getInt(1);
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return isExists;
    }

    public static int createUser(String login, String password, int type, String surname, String name, String patronymic, String phone, String mail) //создание нового пользователя
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("insert into user values (default, '"+ login +"', '"+
                    password + "', " + type + ", '" + surname + "', '" + name + "', '" +
                    patronymic + "', '" + phone + "', '" + mail + "');").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;}
        return success;
    }

    public static int deleteUser(int id) //удаление пользователя
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from user where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public static int updateUserLoginAndPassword(int id, String login, String password) //восстановление пароля
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update user set login='" + login + "', pswd='" + password + "' where id=" + id + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static int updateUser(int id, String login, String password, int type, String surname, String name, String patronymic, String phone, String mail)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update user set login='" + login +
                    "', pswd='" + password + "', type=" + type + ", surname='" + surname +
                    "', name='" + name + "', patronymic='" + patronymic +
                    "', phone='" + phone + "', mail='" + mail +
                    "' where id=" + id + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static int updateUser(UsersTableList user)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update user set type=" + user.getType() + ", surname='" + user.getSurname() +
                    "', name='" + user.getName() + "', patronymic='" + user.getPatronymic() +
                    "', phone='" + user.getPhone() + "', mail='" + user.getMail() +
                    "' where id=" + user.getId() + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static ArrayList<ExamTableList> getExams() //возвращает список всех экзаменов
    {
        ArrayList<ExamTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select exam.id, exam.name, mark_st, exam.id_t, t_name, exam.gr, gr_name, ddate from exam, (select id, CONCAT(surname, \" \", name, \" \", patronymic) as t_name from user) t1, (select id, name as gr_name from groupp) t2 where exam.id_t=t1.id and exam.gr=t2.id;"); //получение списка экзаменов
            while(result.next()) {
                al.add(new ExamTableList(result.getInt("id"), result.getString("name"),
                        result.getInt("mark_st"), result.getInt("id_t"), result.getString("t_name"),
                        result.getInt("gr"), result.getString("gr_name"), result.getString("ddate")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static Exam getExam(int id) //возвращает экзамен по идентификатору
    {
        Exam exam = new Exam();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select * from exam where id=" + id + ";"); //получение экзамена
            if(result.next()) //если экзамен получен
                exam = new Exam(result.getInt("id"), result.getString("name"),
                        result.getInt("mark_st"), result.getInt("id_t"),
                        result.getInt("gr"), result.getString("ddate"));
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return exam;
    }

    public static ArrayList<Exam> getExamsByUID(int id) //возвращает список экзаменов, привязанный к идентификатору пользователя
    {
        ArrayList<Exam> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select * from exam where id_t=" + id + ";"); //получение списка экзаменов
            while(result.next()) {
                al.add(new Exam(result.getInt("id"), result.getString("name"),
                        result.getInt("mark_st"), result.getInt("id_t"),
                        result.getInt("gr"), result.getString("ddate")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static int createExam(String name, int markStatus, int teaherId, int groupId, String date) //создание нового экзамена
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("insert into exam values (default, '" + name +"', " +
                    markStatus + ", " + teaherId + ", " + groupId + ", '" + date + "');").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success = 0;}
        return success;
    }

    public static int updateExam(ExamTableList exam)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update exam set name='" + exam.getName() +
                    "',mark_st=" + exam.getMarkSt() + ",id_t=" + exam.getTeacherID() +
                    ",gr=" + exam.getGroupID() + ",ddate='" + exam.getDate() +"' where id=" + exam.getId() +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static int deleteExam(int id) //удаление экзамена
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from exam where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public static ArrayList<StudentTableList> getStudents() //возвращает список всех студентов
    {
        ArrayList<StudentTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select num, surname, name, patronymic, gr, gr_name, status, phone, mail from student, (select id, name as gr_name from groupp) t1 where gr=t1.id;"); //получение списка студентов
            while(result.next()) {
                al.add(new StudentTableList(result.getInt("num"), result.getString("name"),
                        result.getString("surname"), result.getString("patronymic"),
                        result.getInt("gr"), result.getString("gr_name"), result.getInt("status"),
                        result.getString("phone"), result.getString("mail")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static ArrayList<Student> getStudentsByGrID(int id) //возвращает список студентов по идентификатору группы
    {
        ArrayList<Student> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select * from student where gr=" + id + ";"); //получение списка студентов
            while(result.next()) {
                al.add(new Student(result.getInt("num"), result.getString("name"),
                        result.getString("surname"),result.getString("patronymic"),
                        result.getInt("gr"), result.getInt("status"),
                        result.getString("phone"), result.getString("mail")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static int createStudent(String surname, String name, String patronymic, int group, String phone, String mail) //создание нового студента
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("insert into student values (default, '" +
                    surname + "', '" + name + "', '" + patronymic + "', " +
                    group + ", 1, '" + phone + "', '"+ mail + "');").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success = 0;}
        return success;
    }

    public static int updateStudent(StudentTableList student)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update student set surname='" + student.getSurname() +
                    "',name='" + student.getName() + "',patronymic='" + student.getPatronymic() + "',gr=" + student.getGroupID() +
                    ",status=" + student.getStatus() + ",phone='" + student.getPhone() + "',mail='" + student.getMail() +
                    "' where num=" + student.getId() + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static int deleteStudent(int id) //удаление студента
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from student where num="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public static ArrayList<MarkTableList> getMarks() //возвращает список оценок
    {
        ArrayList<MarkTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select mark.id, exam, exam.name as exam_name, num_rb, CONCAT(student.surname, \" \", student.name, \" \", student.patronymic) as student, mark.id_t, CONCAT(user.surname, \" \", user.name, \" \", user.patronymic) as teacher, mark, mark.ddate, retake from mark, exam, student, user where mark.exam=exam.id and num_rb=num and mark.id_t=user.id;"); //получение списка оценок
            while(result.next()) {
                al.add(new MarkTableList(result.getInt("id"), result.getInt("exam"), result.getString("exam_name"),
                        result.getInt("num_rb"), result.getString("student"), result.getInt("id_t"), result.getString("teacher"),
                        result.getInt("mark"), result.getString("ddate"), result.getInt("retake")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static ArrayList<MarkTableList> getMarksByEID(int id) //возвращает список оценок, привязанных к идентификатору экзамена
    {
        ArrayList<MarkTableList> list = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select id, exam, exam_name, num_rb, student, id_t, teacher, mark, ddate, max(retake) as retake from (select mark.id, exam, exam.name as exam_name, num_rb, CONCAT(student.surname, \" \", student.name, \" \", student.patronymic) as student, mark.id_t, CONCAT(user.surname, \" \", user.name, \" \", user.patronymic) as teacher, mark, mark.ddate, retake from mark, exam, student, user where mark.exam=exam.id and num_rb=num and mark.id_t=user.id and mark.exam=" + id + " order by student, -retake) t group by num_rb;"); //получение списка оценок
            while(result.next()) {
                list.add(new MarkTableList(result.getInt("id"), result.getInt("exam"), result.getString("exam_name"),
                        result.getInt("num_rb"), result.getString("student"), result.getInt("id_t"), result.getString("teacher"),
                        result.getInt("mark"), result.getString("ddate"), result.getInt("retake")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return list;
    }

    public static int updateMark(int id, int mark) //обновление данных об оценке
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update mark set mark=" + mark + " where id=" + id + ";").executeUpdate();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();success = 0;}
        return success;
    }

    public static int updateMark(MarkTableList mark)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update mark set exam=" + mark.getExamID() +
                    ",num_rb=" + mark.getStudentID() + ",id_t=" + mark.getUserID() + ",mark=" + mark.getMark() +
                    ",ddate='" + mark.getDate() + "',retake=" + mark.getRetake() + " where id=" + mark.getId() + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static int createMark(int examId, int studentId, int teacherId, int mark, int retake) //создание оценки (пересдачи)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("insert into mark values (default, " + examId + ", "+
                    studentId + ", " + teacherId + ", " + mark + ", curdate(), " + retake +");").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public static int getNumberOfRetakes(int studentId, int examId) //получить количество сдач у студента за данный экзамен
    {
        int retake = -1;
        // -1 - не удалось подключиться к БД
        // 0 - студент не сдавал данный экзамен
        // >0 - колчество сдач
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select count(retake) from mark where num_rb=" + studentId + " and exam=" + examId + ";");
            if(result.next())
                retake = result.getInt(1);
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return retake;
    }

    public static int deleteMark(int id) //удаление пересдачи (удаление оценки)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from mark where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public static ArrayList<Group> getGroups() //возвращает список групп
    {
        ArrayList<Group> al = new ArrayList<>();
        try{
            ResultSet result = getDBConnection().createStatement().executeQuery("select * from groupp;"); //получение списка групп
            while(result.next()) {
                al.add(new Group(result.getInt("id"),  result.getString("name"),  result.getInt("course"),  result.getInt("year")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static Group getGroupByID(int id) //получение информации о группе, по её идентификатору
    {
        Group gr = new Group();
        try{
            ResultSet result = getDBConnection().createStatement().executeQuery("select * from groupp where id=" + id + ";"); //получение списка экзаменов
            if(result.next())
                gr = new Group(result.getInt("id"),  result.getString("name"),  result.getInt("course"),  result.getInt("year"));
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return gr;
    }

    public static int updateGroup(Group group)
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update groupp set name='" + group.getName() +
                    "', course=" + group.getCourse() + ", year=" + group.getYear() + " where id=" + group.getId() + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }

    public static ArrayList<RatingStudentTableList> getRatingStudents() //возвращает список с рейтингом студентов
    {
        ArrayList<RatingStudentTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select course, gr_name, CONCAT(surname, \" \", name, \" \", patronymic) as student, amark from student, (select num_rb, avg(mark) as amark from mark where mark.mark>=2 group by num_rb) t1, (select id, name as gr_name, course from groupp) t2 where num=num_rb and student.gr=t2.id order by gr, -amark;"); //получение списка рейтинга студентов
            while(result.next()) {
                al.add(new RatingStudentTableList(result.getInt("course"), result.getString("gr_name"),
                        result.getString("student"), result.getDouble("amark")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static int createGroup(String name, int course, int year) //создание новой группы
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("insert into groupp values (default, '" +
                    name + "', " + course + ", " + year + ");").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();success = 0;}
        return success;
    }

    public static int deleteGroup(int id) //удаление группы
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from groupp where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public static ArrayList<RatingGroupTableList> getRatingGroups() //возвращает список с рейтингом студентов
    {
        ArrayList<RatingGroupTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select course, gr_name, aamark from (select course, gr_name, avg(amark) as aamark from (select course, gr, gr_name, num, amark from student, (select num_rb, avg(mark) as amark from mark group by num_rb) t1, (select course, id, name as gr_name from groupp) t2 where num=t1.num_rb and t2.id=gr )t3 group by gr_name) t4 order by course, -aamark;"); //получение списка рейтинга студентов
            while(result.next()) {
                al.add(new RatingGroupTableList(result.getInt("course"), result.getString("gr_name"), result.getDouble("aamark")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static ArrayList<ExpellTableList> getExpellList() //возвращает список студентов "к отчислению"
    {
        ArrayList<ExpellTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select CONCAT(surname, \" \", name, \" \", patronymic) as student, debt from student, (select num_rb, count(mark) as debt from mark where mark.mark=2 or mark.mark=0  group by num_rb having debt>2) t1 where num=num_rb;"); //получение списка студентов
            while(result.next()) {
                al.add(new ExpellTableList(result.getString("student"), result.getInt("debt")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static ArrayList<StipendTableList> getStipendList() //возвращает список студентов "к отчислению"
    {
        ArrayList<StipendTableList> al = new ArrayList<>();
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select gr_name, CONCAT(surname, \" \", name, \" \", patronymic) as student, if(amark>=4, if(amark=5.0, 2000, 800), 0) as stipend from student, (select num, amark from student, (select num_rb, avg(mark) as amark from mark where mark.mark>=2 group by num_rb) t1 where num=num_rb) t1, (select course, id, name as gr_name from groupp) t2 where student.num=t1.num and t2.id=gr order by gr_name, student;"); //получение списка студентов
            while(result.next()) {
                al.add(new StipendTableList(result.getString("gr_name"), result.getString("student"), result.getInt("stipend")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        return al;
    }

    public static int isUserCanBeDeleted(int id) //проверка, привязаны ли какие-то экзамены или оценки к пользователю
    {
        int c = -1;
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select c1, c2 from (select count(id_t) as c1 from exam where id_t=" + id + ") t1, (select count(id_t) as c2 from mark where id_t=" + id + ") t2;"); //подсчёт количества экзаменов и оценок, привязанных к пользователю
            if(result.next())
                c = result.getInt(1) + result.getInt(2);
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        if (c==0) return 1; //пользователя можно удалять
        else if (c==-1) return -1; //не удалось подключиться к БД/ошибка в выполнении запроса
        else return 0; //пользователя нельзя удалить
    }

    public static int isGroupCanBeDeleted(int id) //проверка, можно ли удалить группу (привязаны ли к ней экзамены или студенты)
    {
        int c = -1, c2=0, c3=0;
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select c1, c2 from (select count(num) as c1 from student where gr=" + id + ") t1, (select count(id) as c2 from exam where gr=" + id + ") t2;"); //подсчёт количества экзаменов и оценок, привязанных к пользователю
            if(result.next())
            {
                c2 = result.getInt(1);
                c3 = result.getInt(2);
            }
            c = c2 + c3;
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        if (c==0) return 1; //группу можно удалять
        else if (c==-1) return -1; //не удалось подключиться к БД/ошибка в выполнении запроса
        else
        {
            if(c2>0) return 2; //группу нельзя удалить: к группе привязаны студенты
            else return 3; //группу нельзя удалить: к группе привязаны экзамены
        }
    }

    public static int isStudentCanBeDeleted(int id) //проверка, привязаны ли какие-то оценки к студенту
    {
        int c = -1;
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select count(mark) from mark where num_rb=" + id + ";"); //подсчёт количества оценок, привязанных к студенту
            if(result.next())
                c = result.getInt(1);
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        if (c==0) return 1; //студента можно удалять
        else if (c==-1) return -1; //не удалось подключиться к БД/ошибка в выполнении запроса
        else return 0; //студента нельзя удалить
    }

    public static int isExamCanBeDeleted(int id) //проверка, привязаны ли какие-то оценки к экзамену
    {
        int c = -1;
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select count(mark) from mark where exam=" + id + ";"); //подсчёт количества оценок, привязанных к экзамену
            if(result.next())
                c = result.getInt(1);
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        if (c==0) return 1; //экзамен можно удалять
        else if (c==-1) return -1; //не удалось подключиться к БД/ошибка в выполнении запроса
        else return 0; //экзамен нельзя удалить
    }
}