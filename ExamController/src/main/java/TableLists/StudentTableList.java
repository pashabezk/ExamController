package TableLists;

public class StudentTableList
{
    //статус студента
    public static String STATUS_EDUCATION = "обучается";
    public static String STATUS_EXPEL = "отчислен";
    public static String STATUS_ACADEMIC_LEAVE = "академ отпуск";
    public static String STATUS_FINISH_EDUCATION = "окончил обучение";

    private int id; //идентификатор
    private String name; //имя
    private String surname; //фамилия
    private String patronymic; //отчество  
    private int groupID; //идентификатор группы
    private String group; //название группы  
    private int status;
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
        setStatusSTR();
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

    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setPatronymic(String patronymic) {this.patronymic = patronymic;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setMail(String mail) {this.mail = mail;}
    public void setGroup(int groupID, String groupName)
    {
        this.groupID = groupID;
        this.group = groupName;
    }

    private void setStatusSTR()
    {
        switch(status)
        {
            case 1: statusSTR = STATUS_EDUCATION; break;
            case 2: statusSTR = STATUS_EXPEL; break;
            case 3: statusSTR = STATUS_ACADEMIC_LEAVE; break;
            case 4: statusSTR = STATUS_FINISH_EDUCATION; break;
        }
    }
    public void setStatus(int status)
    {
        this.status = status;
        setStatusSTR();
    }
    
    @Override
    public String toString() {return group + ": " + surname + " " + name + " " + patronymic;}
}