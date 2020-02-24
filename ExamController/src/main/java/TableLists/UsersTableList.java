package TableLists;

public class UsersTableList
{
    //тип пользователя
    public static String TYPE_ADMINISTRATION_STAFF = "Сотрудник администрации";
    public static String TYPE_TEACHER = "Преподаватель";
    public static String TYPE_DATABASE_ADMINISTRATION = "Администратор БД";

    private int id; //идентификатор
    private int type; //тип пользователя
    /* 1 - сотрудник администрации
     * 2 - преподаватель
     * 3 - администратор БД*/
    private String typeSTR; //тип пользователя в виде строки
    private String name; //имя
    private String surname; //фамилия
    private String patronymic; //отчество
    private String phone; //телефон
    private String mail; //почта

    public UsersTableList(int id, int type, String name, String surname, String patronymic, String phone, String mail)
    {
        this.id = id;
        this.type = type;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phone = phone;
        this.mail = mail;
        setTypeSTR();
    }

    public int getId() {return id;}
    public int getType() {return type;}
    public String getTypeSTR() {return typeSTR;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getPatronymic() {return patronymic;}
    public String getPhone() {return phone;}
    public String getMail() {return mail;}

    private void setTypeSTR()
    {
        switch (type)
        {
            case 1: typeSTR = TYPE_ADMINISTRATION_STAFF; break;
            case 2: typeSTR = TYPE_TEACHER; break;
            case 3: typeSTR = TYPE_DATABASE_ADMINISTRATION; break;
        }
    }
    public void setType(int type)
    {
        this.type = type;
        setTypeSTR();
    }
    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setPatronymic(String patronymic) {this.patronymic = patronymic;}
    public void setPhone(String phone) {this.phone = phone; }
    public void setMail(String mail) {this.mail = mail;}

    @Override
    public String toString() {return surname + " " + name + " " + patronymic;}
}
