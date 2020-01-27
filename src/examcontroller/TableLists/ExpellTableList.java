package examcontroller.TableLists;

public class ExpellTableList
{
    private String student;
    private int debt;

    public ExpellTableList(String student, int debt)
    {
        this.student = student;
        this.debt = debt;
    }
    
    public String getStudent() {return student;}
    public int getDebt() {return debt;}
}
