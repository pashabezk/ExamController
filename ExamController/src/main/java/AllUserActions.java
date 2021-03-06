import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//класс хранит действия, которые являются общими для любого типа пользователя
public class AllUserActions
{
    public static void exitAndAuth()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AllUserActions.class.getResource("/Authorization.fxml"));
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setTitle(GLOBAL.TITLE);
        stage.setResizable(false);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }

    public static void openSettingsWindow()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AllUserActions.class.getResource("/Settings.fxml"));
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setTitle(GLOBAL.TITLE + " - Настройки профиля");
        stage.setMinWidth(560);
        stage.setMinHeight(420);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }
}

