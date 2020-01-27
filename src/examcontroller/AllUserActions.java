//класс хранит действия, которые являются общими для любого типа пользователя

package examcontroller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AllUserActions
{
    public void exit()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Authorization.fxml"));                    
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(GLOBAL.TITLE);
        stage.setResizable(false);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }
    
    public void openSettingsWindow()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Settings.fxml"));                    
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(GLOBAL.TITLE + " - Настройки профиля");
        stage.setMinWidth(560);
        stage.setMinHeight(400);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }
}
