package examcontroller;

public class GLOBAL
{
    //глобальный пользователь, который авторизовался
    public static User user = new User();
    
    //используемые константы
    public static String TITLE = "Exam Controller"; //название программы, используется в заголовке окна
    public static String DBURL = "//192.168.1.17/sessia"; //адрес подключения к БД
    public static String ICONURL = "resources/icon.png"; //местоположение иконки приложения
}

//после завершения проекта, посмотри: нужна ли по итогу инкапсуляция полей? Если нет, то затри её

//проблема: при сортировке данных в таблице, нельзя делать update данных, т.к. ломаются индексы

//небольшая некорректность: по умолчанию при создании примечания появляется оценка 2. Для экзаменов, где зач/не зач, такой вариант не подходит

//экран с настройками вообще не работает
//добавление и удаление работает только с пользователями

//на экране администратора БД запись удалена появляется даже когда элементы не выбраны, но кнопка нажата