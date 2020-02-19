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

    public void setName(String name) {this.name = name;}
    public void setCourse(int course) {this.course = course;}
    public void setYear(int year) {this.year = year;}

    @Override
    public String toString() {return this.name;}
}

