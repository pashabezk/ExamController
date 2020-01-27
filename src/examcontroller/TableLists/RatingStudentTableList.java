package examcontroller.TableLists;

public class RatingStudentTableList
{
    private int course;
    private String group;
    private String student;
    private double mark;
    
    public RatingStudentTableList(int course, String group, String student, double mark)
    {
        this.course = course;
        this.group = group;
        this.student = student;
        this.mark = mark;
    }
    
    public int getCourse() {return course;}
    public String getGroup() {return group;}
    public String getStudent() {return student;}
    public double getMark() {return mark;}
}
