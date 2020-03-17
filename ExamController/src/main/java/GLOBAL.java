import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GLOBAL
{
    //глобальный пользователь, который авторизовался
    public static User user = new User();
    
    //используемые константы
    public static String TITLE = "Exam Controller"; //название программы, используется в заголовке окна
    public static String DBURL = "//localhost/sessia"; //адрес подключения к БД
    public static String ICONURL = "/icon.png"; //местоположение иконки приложения

    //используемые строки
    public static String ERROR_DB_CONNECTION = "ОШИБКА: не удалось подключиться к БД";
    public static String SUCCESS_UPDATE = "Запись обновлена";

    //цвета
    public static Paint RED = Color.web("#ff0000");
    public static Paint BLUE = Color.web("#0000ff");

    //временные логин и пароль
    public static String temp_login = "";
    public static String temp_password = "";
}
//на экране администратора БД (и у обычного пользователя) запись удалена появляется даже когда элементы не выбраны, но кнопка нажата

//верни считывание IP-адреса из файла!

//добавить администратору БД возможность принудительно удалять экзамен и оценки

//при выставлении зач/незач появляются оценки

//переместить цвета и строки в константы

//при редактировании и добавлении данных необходимо проверять длину строк