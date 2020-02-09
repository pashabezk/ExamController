public class Student
{
    private int id; //идентификатор
    private String name; //имя
    private String surname; //фамилия
    private String patronymic; //отчество  
    private int group; //идентификатор группы
    private int status; //статус: 1 - учится; 2 - отчислен; 3 - академ отпуск
    private String phone; //телефон
    private String mail; //почта

    public Student(int id, String name, String surname, String patronymic, int group, int status, String phone, String mail)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.group = group;
        this.status = status;
        this.phone = phone;
        this.mail = mail;
    }
    
    public String getFullName() //получение ФИО
    {return surname + " " + name + " " + patronymic;}

    public int getId() {return id;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getPatronymic() {return patronymic;}
    public int getGroup() {return group;}
    public int getStatus() {return status;}
    public String getPhone() {return phone;}
    public String getMail() {return mail;}
}
