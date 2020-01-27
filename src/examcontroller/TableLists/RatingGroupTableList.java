package examcontroller.TableLists;

public class RatingGroupTableList
{
    private int course;
    private String group;
    private double mark;
    
    public RatingGroupTableList(int course, String group, double mark)
    {
        this.course = course;
        this.group = group;
        this.mark = mark;
    }

    public int getCourse() {return course;}
    public String getGroup() {return group;}
    public double getMark() {return mark;}    
}