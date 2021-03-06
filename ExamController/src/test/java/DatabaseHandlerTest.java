import TableLists.ExamTableList;
import TableLists.ExpellTableList;
import TableLists.MarkTableList;
import TableLists.RatingGroupTableList;
import TableLists.RatingStudentTableList;
import TableLists.StipendTableList;
import TableLists.StudentTableList;
import TableLists.UsersTableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseHandlerTest {

    @FXML
    private Label fxErrMsg;

    @Test
    public void hmm(){
        File DBConfig = new File(GLOBAL.DBCONFIGURL);
        if(DBConfig.exists()) //если файл конфигурации БД существует
        {
            try
            {
                FileReader fr = new FileReader(DBConfig);
                BufferedReader reader = new BufferedReader(fr);
                DatabaseHandler.DBURL = reader.readLine();
                DatabaseHandler.DBPORT = reader.readLine();
                DatabaseHandler.DBNAME = reader.readLine();
                DatabaseHandler.MYSQL_USER = reader.readLine();
                DatabaseHandler.MYSQL_PASSWORD = reader.readLine();
            }
            catch (IOException e) {e.printStackTrace();}

            if (DatabaseHandler.DBURL == null || DatabaseHandler.DBPORT == null || DatabaseHandler.DBNAME == null || DatabaseHandler.MYSQL_USER == null || DatabaseHandler.MYSQL_PASSWORD == null)
                fxErrMsg.setText("ошибка в настройках конфигурации");
    }
    }


    @Test
    public void signIn() throws SQLException {
        hmm();
        int actual = DatabaseHandler.signIn("glad", "1234");
        System.out.println("jdbc:mysql://" + DatabaseHandler.DBURL + ":" + DatabaseHandler.DBPORT + "/" + DatabaseHandler.DBNAME);
        int expected = 4;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPassword() throws SQLException {
        hmm();
        int actual = DatabaseHandler.checkPassword(4, "1234");
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getUsers_NO_NULL() {
        hmm();
        ArrayList<UsersTableList> expected = DatabaseHandler.getUsers();
        Assert.assertNotNull(expected);
    }

    @Test
    public void getUserByID() throws SQLException {
        hmm();
        User expected = new User ( 4, 2, "Ирина", "Гладышева", "Владиславовна", "glad", "89059568715", "irglad@mail.ru");
        User actual = DatabaseHandler.getUserByID(4);
        Assert.assertEquals(expected.getLogin(), actual.getLogin());
        Assert.assertEquals(expected.getPatronymic(), actual.getPatronymic());
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getSurname(), actual.getSurname());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
        Assert.assertEquals(expected.getMail(), actual.getMail());
    }

    @Test
    public void isLoginExists() {
        hmm();
        int expected = 1;
        int actual = DatabaseHandler.isLoginExists("glad");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void createUser() throws SQLException {//проверяет бд пользователей на наличие созданного id
        hmm();
        int actual = DatabaseHandler.createUser("test", "1234", 2, "testova", "test","testovna","89059568715", "testd@mail.ru");
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void updateUserLoginAndPassword() {
        hmm();
        int expected = 1;
        int max=0;
        ArrayList<UsersTableList> users = DatabaseHandler.getUsers();
        int k = users.size();
        for(int i=0; i<k; i++){
            if(users.get(i).getId()>max){
                max=users.get(i).getId();
            }
        }
        int actual = DatabaseHandler.updateUserLoginAndPassword(max, "tset", "321");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void updateUser() {
        hmm();
        int expected = 1;
        int max=0;
        ArrayList<UsersTableList> users = DatabaseHandler.getUsers();
        int k = users.size();
        for(int i=0; i<k; i++){
            if(users.get(i).getId()>max){
                max=users.get(i).getId();
            }
        }
        int actual = DatabaseHandler.updateUser(max, "tset", "321", 2, "Testing", "Test", "Testovna", "89811063459", "test@mail.ru");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteUser() {//работает по-любому, делать проверку по бд?
        hmm();
        int expected = 1;
        int max=0;
        ArrayList<UsersTableList> users = DatabaseHandler.getUsers();
        int k = users.size();
        for(int i=0; i<k; i++){
            if(users.get(i).getId()>max){
                max=users.get(i).getId();
            }
        }
        System.out.println(max);
        int actual = DatabaseHandler.deleteUser(max);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getExams_NO_NULL() {
        hmm();
        ArrayList<ExamTableList> expected = DatabaseHandler.getExams();
        Assert.assertNotNull(expected);
    }

    @Test
    public void getExamsByUID() {
        hmm();
        int act=1, exp=1, k=0, a;
        ArrayList<ExamTableList> exams = DatabaseHandler.getExams();
        ArrayList<Exam> examsid = DatabaseHandler.getExamsByUID(4);
        System.out.println(exams.size());
        for(int i=0; i<exams.size(); i++) {
            if (exams.get(i).getTeacherID()==4){
                if (examsid.size()==k){
                    act=1;
                    Assert.assertEquals(exp, act);
                }else {
                    System.out.println(exams.get(i).getName() + "---" + examsid.get(k).getName() + "--" + k);
                    k++;
                }
            }
        }
        Assert.assertEquals(exp, act);
    }

    @Test
    public void createExam() {
        hmm();
        int actual = DatabaseHandler.createExam("Тест", 5, 2, 2, "2020-02-05");
        int expected =1;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteExam() {
        hmm();
        int expected = 1;
        ArrayList<ExamTableList> exams = DatabaseHandler.getExams();
        int k = exams.size();
        int max=0;
        for(int i=0; i<k; i++){
            if(exams.get(i).getId()>max){
                max=exams.get(i).getId();
            }
        }
        int actual = DatabaseHandler.deleteExam(max);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getStudents_NO_NULL() {
        hmm();
        ArrayList<StudentTableList> expected = DatabaseHandler.getStudents();
        Assert.assertNotNull(expected);
    }

    @Test
    public void getStudentsByGrID() {
        hmm();
        int act=1, exp=1, k=0, a;
        ArrayList<StudentTableList> studs = DatabaseHandler.getStudents();
        ArrayList<Student> studsid = DatabaseHandler.getStudentsByGrID(2);
        for(int i=0; i<studs.size(); i++) {
            if (studs.get(i).getGroupID()==2){
                if (studsid.size()==k){
                    act=1;
                    Assert.assertEquals(exp, act);
                }else {
                    System.out.println(studs.get(i).getName() + "---" + studs.get(i).getGroupID() + "--" + k);
                    k++;
                }
            }
        }
        Assert.assertEquals(exp, act);
    }

    @Test
    public void createStudent() {
        hmm();
        int act = DatabaseHandler.createStudent("Дымченко", "Анастасия", "Сергеевна", 1, "89811063459", "test@mail.ru");
        int exp =1;
        Assert.assertEquals(exp, act);
    }

    @Test
    public void deleteStudent() {
        hmm();
        int expected = 1;
        ArrayList<StudentTableList> studs = DatabaseHandler.getStudents();
        int max=0;
        for(int i=0; i<studs.size(); i++){
            if(studs.get(i).getId()>max){
                max=studs.get(i).getId();
            }
        }
        int actual = DatabaseHandler.deleteStudent(max);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getMarks_NO_NULL() {
        hmm();
        ArrayList<MarkTableList> expected = DatabaseHandler.getMarks();
        Assert.assertNotNull(expected);
    }

    @Test
    public void getMarksByEID() {
        hmm();
        int act=1, exp=1, k=0, a;
        ArrayList<MarkTableList> marks = DatabaseHandler.getMarks();
        ArrayList<MarkTableList> marksid = DatabaseHandler.getMarksByEID(4);
        for(int i=0; i<marks.size(); i++) {
            if (marks.get(i).getExamID()==4){
                if (marksid.size()==k){
                    act=1;
                    Assert.assertEquals(exp, act);
                }else {
                    System.out.println(marks.get(i).getId() + "---" + marksid.get(k).getExamID() + "--" + k);
                    k++;
                }
            }
        }
        Assert.assertEquals(exp, act);
    }

    @Test
    public void createMark() {
        hmm();
        int act =DatabaseHandler.createMark(1, 1, 2, 3, 1);
        int exp =1;
        Assert.assertEquals(exp, act);
    }

    @Test
    public void updateMark() {
        hmm();
        int expected = 1;
        ArrayList<MarkTableList> marks = DatabaseHandler.getMarks();
        int max=0;
        for(int i=0; i<marks.size(); i++){
            if(marks.get(i).getId()>max){
                max=marks.get(i).getId();
            }
        }
        int actual = DatabaseHandler.updateMark(max, 5);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getNumberOfRetakes() {
        hmm();
        int act=0, exp=1;
        int actual = DatabaseHandler.getNumberOfRetakes(1,1);
        if(actual == 0){
            Assert.assertEquals(exp, act);
        }else if(actual == -1){
            Assert.assertEquals(exp, act);
        }else {
            act=1;
            Assert.assertEquals(exp, act);
        }
    }

    @Test
    public void deleteMark() {
        hmm();
        int expected = 1;
        ArrayList<MarkTableList> marks = DatabaseHandler.getMarks();
        int max=0;
        for(int i=0; i<marks.size(); i++){
            if(marks.get(i).getId()>max){
                max=marks.get(i).getId();
            }
        }
        int actual = DatabaseHandler.deleteMark(max);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroups_NO_NULL() {
        hmm();
        ArrayList<Group> expected = DatabaseHandler.getGroups();
        Assert.assertNotNull(expected);
    }

    @Test
    public void getGroupByID() {
        hmm();
        Group expected = new Group (4, "Y2232", 2, 2017);
        Group actual = DatabaseHandler.getGroupByID(4);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getCourse(), actual.getCourse());
        Assert.assertEquals(expected.getYear(), actual.getYear());
    }

    @Test
    public void getRatingStudents() throws SQLException {
        hmm();
        ArrayList<RatingStudentTableList> expected = new ArrayList<>();
        try {
            ResultSet result = DatabaseHandler.getDBConnection().createStatement().executeQuery("select course, gr_name, CONCAT(surname, \" \", name, \" \", patronymic) as student, amark from student, (select num_rb, avg(mark) as amark from mark where mark.mark>=2 group by num_rb) t1, (select id, name as gr_name, course from groupp) t2 where num=num_rb and student.gr=t2.id order by gr, -amark;"); //получение списка рейтинга студентов
            while(result.next()) {
                expected.add(new RatingStudentTableList(result.getInt("course"), result.getString("gr_name"),
                        result.getString("student"), result.getDouble("amark")));
            }
        } catch(SQLException e) {e.printStackTrace();}
        //ResultSet expected = DatabaseHandler.getDBConnection().createStatement().executeQuery("select course, gr_name, CONCAT(surname, \" \", name, \" \", patronymic) as student, amark from student, (select num_rb, avg(mark) as amark from mark where mark.mark>=2 group by num_rb) t1, (select id, name as gr_name, course from groupp) t2 where num=num_rb and student.gr=t2.id order by gr, -amark;"); //получение списка рейтинга студентов
        ArrayList<RatingStudentTableList> actual = DatabaseHandler.getRatingStudents();
        for(int i=0; i<expected.size(); i++)
        {
            Assert.assertEquals(expected.get(i).getStudent(), actual.get(i).getStudent());
        }
    }

    @Test
    public void createGroup() {
        hmm();
        int act = DatabaseHandler.createGroup("Test", 1, 2020);
        int exp = 1;
        Assert.assertEquals(exp, act);
    }

    @Test
    public void deleteGroup() {
        hmm();
        int expected = 1;
        ArrayList<Group> groups = DatabaseHandler.getGroups();
        int max=0;
        for(int i=0; i<groups.size(); i++){
            if(groups.get(i).getId()>max){
                max=groups.get(i).getId();
            }
        }
        int actual = DatabaseHandler.deleteGroup(max);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getRatingGroups() {
        hmm();
        ArrayList<RatingGroupTableList> expected = new ArrayList<>();
        try {
            ResultSet result = DatabaseHandler.getDBConnection().createStatement().executeQuery("select course, gr_name, aamark from (select course, gr_name, avg(amark) as aamark from (select course, gr, gr_name, num, amark from student, (select num_rb, avg(mark) as amark from mark group by num_rb) t1, (select course, id, name as gr_name from groupp) t2 where num=t1.num_rb and t2.id=gr )t3 group by gr_name) t4 order by course, -aamark;"); //получение списка рейтинга студентов
            while(result.next()) {
                expected.add(new RatingGroupTableList(result.getInt("course"), result.getString("gr_name"), result.getDouble("aamark")));
            }
        } catch(SQLException e) {e.printStackTrace();}
        ArrayList<RatingGroupTableList> actual = DatabaseHandler.getRatingGroups();
        for(int i=0; i<expected.size(); i++)
        {
            Assert.assertEquals(expected.get(i).getGroup(), actual.get(i).getGroup());
        }
    }

    @Test
    public void getExpellList() {
        hmm();
        ArrayList<ExpellTableList> expected = new ArrayList<>();
        try {
            ResultSet result = DatabaseHandler.getDBConnection().createStatement().executeQuery("select CONCAT(surname, \" \", name, \" \", patronymic) as student, debt from student, (select num_rb, count(mark) as debt from mark where mark.mark=2 or mark.mark=0  group by num_rb having debt>2) t1 where num=num_rb;"); //получение списка студентов
            while(result.next()) {
                expected.add(new ExpellTableList(result.getString("student"), result.getInt("debt")));
            }
        } catch(SQLException e) {e.printStackTrace();}
        ArrayList<ExpellTableList> actual = DatabaseHandler.getExpellList();
        for(int i=0; i<expected.size(); i++)
        {
            Assert.assertEquals(expected.get(i).getStudent(), actual.get(i).getStudent());
        }
    }

    @Test
    public void getStipendList() {
        hmm();
        ArrayList<StipendTableList> expected = new ArrayList<>();
        try {
            ResultSet result = DatabaseHandler.getDBConnection().createStatement().executeQuery("select gr_name, CONCAT(surname, \" \", name, \" \", patronymic) as student, if(amark>=4, if(amark=5.0, 2000, 800), 0) as stipend from student, (select num, amark from student, (select num_rb, avg(mark) as amark from mark where mark.mark>=2 group by num_rb) t1 where num=num_rb) t1, (select course, id, name as gr_name from groupp) t2 where student.num=t1.num and t2.id=gr order by gr_name, student;"); //получение списка студентов
            while(result.next()) {
                expected.add(new StipendTableList(result.getString("gr_name"), result.getString("student"), result.getInt("stipend")));
            }
        } catch(SQLException e) {e.printStackTrace();}
        ArrayList<StipendTableList> actual = DatabaseHandler.getStipendList();
        for(int i=0; i<expected.size(); i++)
        {
            Assert.assertEquals(expected.get(i).getStudent(), actual.get(i).getStudent());
        }
    }

    @Test
    public void isUserCanBeDeleted() {
        hmm();
        int expected = 0;
        int actual = DatabaseHandler.isUserCanBeDeleted(4);
        if (actual == 0) {
            expected=0;
            Assert.assertEquals(expected, actual);
            System.out.println("user can not be deleted");
        }else if (actual == 1)
        {
            expected=1;
            Assert.assertEquals(expected, actual);
            System.out.println("user can be deleted");
        } else {
            expected=-1;
            Assert.assertEquals(expected, actual);
            System.out.println("error");
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isGroupCanBeDeleted() {
        hmm();
        int expected = 0;
        int actual = DatabaseHandler.isGroupCanBeDeleted(4);
        if (actual == 2) {
            expected=2;
            Assert.assertEquals(expected, actual);
            System.out.println("group can not be deleted --- students attached");
        }else if (actual == 1)
        {
            expected=1;
            Assert.assertEquals(expected, actual);
            System.out.println("group can be deleted");
        } else if(actual == -1){
            expected=-1;
            Assert.assertEquals(expected, actual);
            System.out.println("error");
        }else{
            expected=3;
            Assert.assertEquals(expected, actual);
            System.out.println("group can not be deleted --- exams attached");
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isStudentCanBeDeleted() {
        hmm();
        int expected = 0;
        int actual = DatabaseHandler.isStudentCanBeDeleted(4);
        if (actual == 0) {
            expected=0;
            Assert.assertEquals(expected, actual);
            System.out.println("student can not be deleted");
        }else if (actual == 1)
        {
            expected=1;
            Assert.assertEquals(expected, actual);
            System.out.println("student can be deleted");
        } else {
            expected=-1;
            Assert.assertEquals(expected, actual);
            System.out.println("error");
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isExamCanBeDeleted() {
        hmm();
        int expected = 0;
        int actual = DatabaseHandler.isExamCanBeDeleted(4);
        if (actual == 0) {
            expected=0;
            Assert.assertEquals(expected, actual);
            System.out.println("exam can not be deleted");
        }else if (actual == 1)
        {
            expected=1;
            Assert.assertEquals(expected, actual);
            System.out.println("exam can be deleted");
        } else {
            expected=-1;
            Assert.assertEquals(expected, actual);
            System.out.println("error");
        }
        Assert.assertEquals(expected, actual);
      }
}