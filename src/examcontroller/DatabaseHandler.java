//в этом классе прописываются команды для управления БД

package examcontroller;

import examcontroller.TableLists.ExamTableList;
import examcontroller.TableLists.ExpellTableList;
import examcontroller.TableLists.MarkTableList;
import examcontroller.TableLists.RatingGroupTableList;
import examcontroller.TableLists.RatingStudentTableList;
import examcontroller.TableLists.StipendTableList;
import examcontroller.TableLists.StudentTableList;
import examcontroller.TableLists.UsersTableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler
{
    Connection DBConnection = null;
    
    public Connection getDBConnection() throws SQLException
    {        
        if (DBConnection == null) //проверка не открыт ли уже доступ к БД
        {
            Properties p=new Properties(); //создание параметров для подключения к БД
            p.setProperty("user","user1");
            p.setProperty("password","1234");
            p.setProperty("useUnicode","true");
            p.setProperty("characterEncoding","cp1251");
            DBConnection = DriverManager.getConnection("jdbc:mysql:"+GLOBAL.DBURL,p); //создание подключения к БД
        }        
        return DBConnection;
    }
    
    public void closeDB() throws SQLException
    {
        DBConnection.close();
        DBConnection = null;
    }
    
    public int signIn(String login, String password)
    {
        int ret = -1;
        /* -1 - не удалось подключиться к БД
         * 0 - пользователь не найден (неправильный логин/пароль)
         * в случае успешного подключения взвращается идентификатор пользователя
        */
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
    
    public int checkPassword(int id, String password)
    {
        int ret = -1;
        /* -1 - не удалось подключиться к БД
         * 0 - неправильный пароль
         * 1 - правильный пароль
        */
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
    
    public ArrayList<UsersTableList> getUsers() //получение списка всех пользователей
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
    
    public User getUserByID(int id) //получение информации о пользователе по идентификатору
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
    
    public int isLoginExists(String login) //проверка, существует ли такой логин в системе
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
    
    public void createUser(String login, String password, int type, String surname, String name, String patronymic, String phone, String mail) //создание нового пользователя
    {
        try {
            getDBConnection().prepareStatement("insert into user values (default, '"+ login +"', '"+
                    password + "', " + type + ", '" + surname + "', '" + name + "', '" +
                    patronymic + "', '" + phone + "', '" + mail + "');").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public int deleteUser(int id) //удаление пользователя
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from user where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }
    
    public int updateUserLoginAndPassword(int id, String login, String password) //восстановление пароля
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("update user set login='" + login + "', pswd='" + password + "' where id=" + id + ";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось обновить
        return success;
    }
    
    public int updateUser(int id, String login, String password, int type, String surname, String name, String patronymic, String phone, String mail)
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
    
    public ArrayList<ExamTableList> getExams() //возвращает список всех экзаменов
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
    
    public ArrayList<Exam> getExamsByUID(int id) //возвращает список экзаменов, привязанный к идентификатору пользователя
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
    
    public void createExam(String name, int markStatus, int teaherId, int groupId, String date) //создание нового экзамена
    {
        try {
            getDBConnection().prepareStatement("insert into exam values (default, '" + name +"', " +
                    markStatus + ", " + teaherId + ", " + groupId + ", '" + date + "');").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public int deleteExam(int id) //удаление экзамена
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from exam where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }
    
    public ArrayList<StudentTableList> getStudents() //возвращает список всех студентов
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
    
    public ArrayList<Student> getStudentsByGrID(int id) //возвращает список студентов по идентификатору группы
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
    
    public void createStudent(String surname, String name, String patronymic, int group, String phone, String mail) //создание нового студента
    {
        try {
            getDBConnection().prepareStatement("insert into student values (default, '" +
                    surname + "', '" + name + "', '" + patronymic + "', " +
                    group + ", 1, '" + phone + "', '"+ mail + "');").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public int deleteStudent(int id) //удаление студента
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from student where num="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }
    
    public ArrayList<MarkTableList> getMarks() //возвращает список оценок
    {
        ArrayList<MarkTableList> al = new ArrayList<>();        
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select mark.id, exam, exam.name as exam_name, num_rb, CONCAT(student.surname, \" \", student.name, \" \", student.patronymic) as student, mark.id_t, CONCAT(user.surname, \" \", user.name, \" \", user.patronymic) as teacher, mark, mark.ddate, retake from mark, exam, student, user where mark.id=exam.id and num_rb=num and user.id=mark.id_t;"); //получение списка оценок
            while(result.next()) {
                al.add(new MarkTableList(result.getInt("id"), result.getInt("exam"), result.getString("exam_name"),
                    result.getInt("num_rb"), result.getString("student"), result.getInt("id_t"), result.getString("teacher"),
                    result.getInt("mark"), result.getString("ddate"), result.getInt("retake")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}        
        return al;
    }
    
    public ObservableList<Mark> getMarksByEID(int id) //возвращает список оценок, привязанных к идентификатору экзамена
    {
        ObservableList<Mark> list = FXCollections.observableArrayList();        
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select * from mark where exam=" + id + ";"); //получение списка экзаменов
            while(result.next()) {
                list.add(new Mark(result.getInt("id"), result.getInt("exam"),
                    result.getInt("num_rb"), result.getInt("id_t"),
                    result.getInt("mark"), result.getString("ddate"), result.getInt("retake")));
            }
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
        return list;
    }
    
    public void updateMark(Mark mark) //обновление данных об оценке
    {
        try {
            getDBConnection().prepareStatement("update mark set mark="+mark.getMark()+" where id="+mark.getId()+";").executeUpdate();            
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public void createMark(int examId, int studentId, int retake) //создание пересдачи (внесение новой оценки в таблицу)
    {
        try {
            getDBConnection().prepareStatement("insert into mark values (default, "+examId+", "+
                    studentId + ", " + GLOBAL.user.getId() + ", 2, curdate(), " + retake +");").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public void deleteMark(int id) //удаление пересдачи (удаление оценки)
    {
        try {
            getDBConnection().prepareStatement("delete from mark where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public ArrayList<Group> getGroups() //возвращает список групп
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
    
    public Group getGroupByID(int id) //получение информации о группе, по её идентификатору
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
    
    public ArrayList<RatingStudentTableList> getRatingStudents() //возвращает список с рейтингом студентов
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
    
    public void createGroup(String name, int course, int year) //создание новой группы
    {
        try {
            getDBConnection().prepareStatement("insert into groupp values (default, '" +
                    name + "', " + course + ", " + year + ");").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace();} 
    }
    
    public int deleteGroup(int id) //удаление группы
    {
        int success = 1;
        try {
            getDBConnection().prepareStatement("delete from groupp where id="+ id +";").execute();
            closeDB();
        } catch(SQLException e) {e.printStackTrace(); success=0;} //установка success в ноль - не удалось удалить
        return success;
    }

    public ArrayList<RatingGroupTableList> getRatingGroups() //возвращает список с рейтингом студентов
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
    
    public ArrayList<ExpellTableList> getExpellList() //возвращает список студентов "к отчислению"
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
    
    public ArrayList<StipendTableList> getStipendList() //возвращает список студентов "к отчислению"
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
    
    public int isUserCanBeDeleted(int id) //проверка, привязаны ли какие-то экзамены или оценки к пользователю
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
    
    public int isGroupCanBeDeleted(int id) //проверка, можно ли удалить группу (привязаны ли к ней экзамены или студенты)
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
    
    public int isStudentCanBeDeleted(int id) //проверка, привязаны ли какие-то оценки к студенту
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
    
    public int isExamCanBeDeleted(int id) //проверка, привязаны ли какие-то оценки к экзамену
    {
        int c = -1;
        try {
            ResultSet result = getDBConnection().createStatement().executeQuery("select count(mark) from mark where exam=" + id + ";"); //подсчёт количества оценок, привязанных к экзамену
            if(result.next())
                c = result.getInt(1);
            closeDB();
        } catch(SQLException e) {e.printStackTrace();}
        if (c==0) return 1; //студента можно удалять
        else if (c==-1) return -1; //не удалось подключиться к БД/ошибка в выполнении запроса
        else return 0; //студента нельзя удалить
    }
}