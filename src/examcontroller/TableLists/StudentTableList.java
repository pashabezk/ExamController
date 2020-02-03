package examcontroller.TableLists;

public class StudentTableList
{
    private int id; //идентификатор
    private String name; //имя
    private String surname; //фамилия
    private String patronymic; //отчество  
    private int groupID; //идентификатор группы
    private String group; //название группы  
    private int status; //статус: 1 - учится; 2 - отчислен; 3 - академ отпуск
    private String statusSTR; //статус в виде строки
    private String phone; //телефон
    private String mail; //почта

    public StudentTableList(int id, String name, String surname, String patronymic, int groupID, String group, int status, String phone, String mail)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.groupID = groupID;
        this.group = group;
        this.status = status;
        this.phone = phone;
        this.mail = mail;
        
        switch(status)
        {
            case 1: statusSTR = "обучение"; break;
            case 2: statusSTR = "отчислен"; break;
            case 3: statusSTR = "академ отпуск"; break;
        }
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getPatronymic() {return patronymic;}
    public int getGroupID() {return groupID;}
    public String getGroup() {return group;}
    public int getStatus() {return status;}
    public String getStatusSTR() {return statusSTR;}
    public String getPhone() {return phone;}
    public String getMail() {return mail;}
    
    @Override
    public String toString() {return group + ": " + surname + " " + name + " " + patronymic;}
}
