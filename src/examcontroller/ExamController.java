package examcontroller;

import javafx.application.Application;
import javafx.stage.Stage;

public class ExamController extends Application
{    
    @Override
    public void start(Stage stage) throws Exception
    {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(new File("src/config.txt")));
//            GLOBAL.DBURL = "//" + reader.readLine() + "/sessia"; //получение IP адреса устройства, на котором установлена БД
//        } catch (FileNotFoundException e) {e.printStackTrace();}

        AllUserActions.exitAndAuth();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}
