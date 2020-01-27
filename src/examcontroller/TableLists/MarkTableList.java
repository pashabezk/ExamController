package examcontroller.TableLists;

public class MarkTableList
{
    private int id; //идентификатор
    private int examID; //идентификатор экзамена
    private String exam; //экзамен
    private int studentID; //идентификатор студента
    private String student; //ФИО студента
    private int userID; //идентификатор преподавателя
    private String user; //ФИО преподавателя
    private int mark; //оценка
    private String date; //дата сдачи
    private int retake; //номер пересдачи

    public MarkTableList(int id, int examID, String exam, int studentID, String student, int userID, String user, int mark, String date, int retake)
    {
        this.id = id;
        this.examID = examID;
        this.exam = exam;
        this.studentID = studentID;
        this.student = student;
        this.userID = userID;
        this.user = user;
        this.mark = mark;
        this.date = date;
        this.retake = retake;
    }
    
    public int getId() {return id;}
    public int getExamID() {return examID;}
    public String getExam() {return exam;}
    public int getStudentID() {return studentID;}
    public String getStudent() {return student;}
    public int getUserID() {return userID;}
    public String getUser() {return user;}
    public int getMark() {return mark;}
    public String getDate() {return date;}
    public int getRetake() {return retake;}
}
