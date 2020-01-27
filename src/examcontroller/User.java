package examcontroller;

public class User
{
    private int id; //идентификатор
    private int type; //тип пользователя
    /* 1 - сотрудник администрации
     * 2 - преподаватель
     * 3 - администратор БД*/
    
    private String name; //имя
    private String surname; //фамилия
    private String patronymic; //отчество
    private String login; //логин
    private String phone; //телефон
    private String mail; //почта
    
    public User(int id, int type, String name, String surname, String patronymic, String login, String phone, String mail)
    {
        this.id = id;
        this.type = type;
        this.name = name;
        this.surname = surname;        
        this.login = login;
        this.patronymic = patronymic;
        this.phone = phone;
        this.mail = mail;
    }
    
    public User()
    {
        id = 0;
        type = -1;
        name = "unknown";
        surname = "unknown";
        patronymic = "unknown";
        login = "unknown";
        phone = "null";
        mail = "null";
    }
    
    public int getId() {return id;}
    public int getType() {return type;}   
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getPatronymic() {return patronymic;}
    public String getLogin() {return login;}
    public String getPhone() {return phone;}
    public String getMail() {return mail;}
}
