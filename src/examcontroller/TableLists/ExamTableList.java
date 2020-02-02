package examcontroller.TableLists;

public class ExamTableList
{
    private int id; //идентификатор
    private String name; //название экзамена
    private String mark_st; //тип экзамена: зач/незач, оценка
    private int teacherId; //идентификатор преподавателя
    private String teacher; //преподаватель 
    private int groupId; //идентификатор группы
    private String group; //группа, у которой проводится экзамен    
    private String date; //дата экзамена
    
    public static String MARK_ST_DIFF = "диф"; //дифференцированный зачёт
    public static String MARK_ST_NOT_DIFF = "н/диф"; //недифференцированный зачёт

    public ExamTableList(int id, String name, int mark_st, int teacherId, String teacher, int groupId, String group, String date)
    {
        this.id = id;
        this.name = name;
        if(mark_st==1)  this.mark_st = MARK_ST_DIFF;
        else this.mark_st = MARK_ST_NOT_DIFF;
        this.teacherId = teacherId;
        this.teacher = teacher;
        this.groupId = groupId;
        this.group = group;
        this.date = date;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getMark_st() {return mark_st;}
    public int getTeacherID(){return teacherId;}
    public String getTeacher() {return teacher;}
    public int getGroupID() {return groupId;}
    public String getGroup() {return group;}
    public String getDate() {return date;}
    
    @Override
    public String toString() {return name + " - " + teacher + " (" + date + ")";}
}
