package examcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ExamController extends Application
{    
    @Override
    public void start(Stage stage) throws Exception
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/config.txt")));
            GLOBAL.DBURL = "//" + reader.readLine() + "/sessia"; //получение IP адреса устройства, на котором установлена БД
        } catch (FileNotFoundException e) {e.printStackTrace();}
        
        Parent root = FXMLLoader.load(getClass().getResource("Authorization.fxml"));        
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(GLOBAL.TITLE);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}
