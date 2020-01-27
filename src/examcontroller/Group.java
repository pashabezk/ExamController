package examcontroller;

public class Group
{
    private int id; //идентификатор
    private String name; //имя
    private int course; //курс
    private int year; //год начала обучения (год создания группы)

    public Group(int id, String name, int course, int year)
    {
        this.id = id;
        this.name = name;
        this.course = course;
        this.year = year;
    }
    
    public Group()
    {
        id = 0;
        name = "unknown";
        course = 0;
        year = 0;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public int getCourse() {return course;}
    public int getYear() {return year;}
}
