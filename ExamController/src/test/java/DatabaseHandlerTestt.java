import org.junit.Assert;
import org.junit.Test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

public class DatabaseHandlerTestt {

    public static String DBURL = ""; //URL сервера
    public static String DBPORT = ""; //порт сервера
    public static String DBNAME = ""; //название БД
    public static String MYSQL_USER = ""; //пользователь MySQL
    public static String MYSQL_PASSWORD = ""; //пароль пользователя MySQL
    private static Connection DBConnection = null;

    public static Connection getDBConnection() throws SQLException
    {
        if (DBConnection == null) //проверка не открыт ли уже доступ к БД
        {
            Properties p=new Properties(); //создание параметров для подключения к БД
            p.setProperty("user", MYSQL_USER);
            p.setProperty("password", MYSQL_PASSWORD);
            p.setProperty("useUnicode","true");
            p.setProperty("characterEncoding","cp1251");
            DBConnection = DriverManager.getConnection("jdbc:mysql://" + DBURL + ":" + DBPORT + "/" + DBNAME,p); //создание подключения к БД
        }
        return DBConnection;
    }

    public static void closeDB() throws SQLException
    {
        DBConnection.close();
        DBConnection = null;
    }

    @Test
    public void signIn() throws SQLException {
            int actual = DatabaseHandler.signIn("glad", "1234");
            int expected = 4;
            Assert.assertEquals(expected, actual);
    }
}