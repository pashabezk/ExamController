package examcontroller;

public class Exam
{
    private int id; //идентификатор
    private String name; //название экзамена
    private int mark_st; //тип оценки: 1 - зач/незач, 2 - оценка
    private int id_t; //идентификатор преподавателя
    private int group; //группа, у которой проводится экзамен
    private String date; //дата экзамена
    
    public Exam(int id, String name, int mark_st, int id_t, int group, String date)
    {
        this.id = id;
        this.name = name;
        this.mark_st = mark_st;
        this.id_t = id_t;
        this.group = group;
        this.date = date;
    }
    
    public Exam()
    {
        id = 0;
        name = "unknown";
        mark_st = 0;
        id_t = 0;
        group = 0;
        date = "unknown";
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public int getMark_st() {return mark_st;}
    public int getId_t(){return id_t;}
    public int getGroup() {return group;}
    public String getDate() {return date;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setMark_st(int mark_st) {this.mark_st = mark_st;}
    public void setId_t(int id_t) {this.id_t = id_t;}
    public void setGroup(int group) {this.group = group;}
    public void setDate(String date) {this.date = date;}
}
