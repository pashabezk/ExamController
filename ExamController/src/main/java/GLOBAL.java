import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GLOBAL
{
    //глобальный пользователь, который авторизовался
    public static User user = new User();
    
    //используемые константы
    public static String TITLE = "Exam Controller"; //название программы, используется в заголовке окна
    public static String ICONURL = "/icon.png"; //местоположение иконки приложения
    public static String DBCONFIGURL = "DB.conf"; //местоположение файла конфигурации БД

    //используемые строки
    public static String ERROR_DB_CONNECTION = "не удалось подключиться к БД";
    public static String ERROR_EMPTY_FIELDS = "не все поля заполнены";
    public static String SUCCESS_UPDATE = "Запись обновлена";

    //цвета
    public static Paint RED = Color.web("#ff0000");
    public static Paint BLUE = Color.web("#0000ff");

    //временные логин и пароль
    public static String TEMP_LOGIN = "";
    public static String TEMP_PASSWORD = "";
}
//на экране администратора БД (и у обычного пользователя) запись удалена появляется даже когда элементы не выбраны, но кнопка нажата

//верни считывание IP-адреса из файла!

//добавить администратору БД возможность принудительно удалять экзамен и оценки

//при выставлении зач/незач появляются оценки

//переместить цвета и строки в константы

//при редактировании и добавлении данных необходимо проверять длину строк