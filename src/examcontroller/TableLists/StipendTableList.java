package examcontroller.TableLists;

public class StipendTableList
{
    private String group;
    private String Student;
    private int stipend;
    
    public StipendTableList(String group, String Student, int stipend)
    {
        this.group = group;
        this.Student = Student;
        this.stipend = stipend;
    }
    
    public String getGroup() {return group;}
    public String getStudent() {return Student;}
    public int getStipend() {return stipend;}
}
