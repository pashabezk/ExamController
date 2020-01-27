package examcontroller;

public class Mark
{
    private int id; //идентификатор
    private int examID; //идентификатор экзамена
    private int studentID; //идентификатор студента
    private int userID; //идентификатор преподавателя
    private int mark; //оценка
    private String date; //дата сдачи
    private int retake; //номер пересдачи

    public Mark(int id, int examID, int studentID, int userID, int mark, String date, int retake)
    {
        this.id = id;
        this.examID = examID;
        this.studentID = studentID;
        this.userID = userID;
        this.mark = mark;
        this.date = date;
        this.retake = retake;
    }
    
    public Mark()
    {
        id = 0;
        examID = 0;
        studentID = 0;
        userID = 0;
        mark = 0;
        date = "unknown";
        retake = 0;
    }

    public int getId() {return id;}
    public int getExamID() {return examID;}
    public int getStudentID() {return studentID;}
    public int getUserID() {return userID;}
    public int getMark() {return mark;}
    public String getDate() {return date;}
    public int getRetake() {return retake;}

    public void setMark(int mark) {this.mark = mark;}
}
