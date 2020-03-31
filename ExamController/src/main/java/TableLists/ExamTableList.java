package TableLists;

public class ExamTableList
{
    private int id; //идентификатор
    private String name; //название экзамена
    private int markSt; //тип экзамена: зач/незач, оценка в виде числа
    private String markStSTR; //тип экзамена: зач/незач, оценка в виде строки
    private int teacherId; //идентификатор преподавателя
    private String teacher; //преподаватель 
    private int groupId; //идентификатор группы
    private String group; //группа, у которой проводится экзамен    
    private String date; //дата экзамена
    
    public static final String MARK_ST_DIFF = "диф"; //дифференцированный зачёт
    public static final String MARK_ST_NOT_DIFF = "н/диф"; //недифференцированный зачёт

    public ExamTableList(int id, String name, int markSt, int teacherId, String teacher, int groupId, String group, String date)
    {
        this.id = id;
        this.name = name;
        this.markSt = markSt;
        this.teacherId = teacherId;
        this.teacher = teacher;
        this.groupId = groupId;
        this.group = group;
        this.date = date;
        setMarkStSTR();
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public int getMarkSt() {return markSt;}
    public String getMarkStSTR() {return markStSTR;}
    public int getTeacherID(){return teacherId;}
    public String getTeacher() {return teacher;}
    public int getGroupID() {return groupId;}
    public String getGroup() {return group;}
    public String getDate() {return date;}

    public void setName(String name) {this.name = name;}
    public void setDate(String date) {this.date = date;}

    public void setMarkSt(int markSt)
    {
        this.markSt = markSt;
        setMarkStSTR();
    }

    private void setMarkStSTR()
    {
        if(markSt==1)  markStSTR = MARK_ST_NOT_DIFF;
        else markStSTR = MARK_ST_DIFF;
    }

    public void setTeacher(int teacherId, String teacherFullName)
    {
        this.teacherId = teacherId;
        this.teacher = teacherFullName;
    }

    public void setGroup(int groupId, String groupName)
    {
        this.groupId = groupId;
        this.group = groupName;
    }

    @Override
    public String toString() {return name + " - " + teacher + " (" + date + ")";}
}
