import animations.Shake;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AuthorizationController implements Initializable
{
    @FXML private TextField fxLogin;
    @FXML private PasswordField fxPassword;
    @FXML private Label fxErrMsg;
    @FXML private ImageView fxSettings;

    @FXML
    private void handleButtonAuthorization(ActionEvent event)
    {
        Shake errMsgAnim = new Shake(fxErrMsg);

        File DBConfig = new File(GLOBAL.DBCONFIGURL);
        if(DBConfig.exists()) //если файл конфигурации БД существует
        {
            try
            {
                FileReader fr = new FileReader(DBConfig);
                BufferedReader reader = new BufferedReader(fr);
                DatabaseHandler.DBURL = reader.readLine();
                DatabaseHandler.DBPORT = reader.readLine();
                DatabaseHandler.DBNAME = reader.readLine();
                DatabaseHandler.MYSQL_USER = reader.readLine();
                DatabaseHandler.MYSQL_PASSWORD = reader.readLine();
            }
            catch (IOException e) {e.printStackTrace();}

            if (DatabaseHandler.DBURL == null || DatabaseHandler.DBPORT == null || DatabaseHandler.DBNAME == null || DatabaseHandler.MYSQL_USER == null || DatabaseHandler.MYSQL_PASSWORD == null)
                fxErrMsg.setText("ошибка в настройках конфигурации");
            else
            {
                if((fxPassword.getText().equals(""))||(fxLogin.getText().equals("")))
                    fxErrMsg.setText(GLOBAL.ERROR_EMPTY_FIELDS);
                else
                {
                    int userID = DatabaseHandler.signIn(fxLogin.getText(), fxPassword.getText());

                    switch (userID)
                    {
                        case -1:
                            fxErrMsg.setText(GLOBAL.ERROR_DB_CONNECTION);
                            break;

                        case 0:
                            fxErrMsg.setText("неправильный логин или пароль");
                            break;

                        default:
                            fxErrMsg.setText(""); //убрать лейбл с выводом ошибок
                            ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть окно аторизации

                            GLOBAL.user = DatabaseHandler.getUserByID(userID); //получение параметров авторизовавшегося пользователя

                            FXMLLoader loader = new FXMLLoader();
                            switch (GLOBAL.user.getType()) //загрузка разных страниц в зависимости, от типа пользователя
                            {
                                case 1:
                                case 2: loader.setLocation(getClass().getResource("/MainWindow.fxml")); break;
                                case 3: loader.setLocation(getClass().getResource("/AMainWindow.fxml")); break;
                            }

                            try{
                                loader.load();
                            } catch (IOException ex) {ex.printStackTrace();}

                            Parent root = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle(GLOBAL.TITLE);
                            stage.setMinHeight(400);
                            stage.setMinWidth(800);
                            stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
                            stage.showAndWait();
                            break;
                    }
                }
            }
        }
        else fxErrMsg.setText("не заданы настройки конфигурации");
        errMsgAnim.playAnim();
    }

    @FXML
    private void handleSettingsClicked(MouseEvent mouseEvent) //нажата кнопка настроек
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AllUserActions.class.getResource("/DBConfig.fxml"));
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(GLOBAL.TITLE + " - конфигурация БД");
        stage.setMinWidth(560);
        stage.setMinHeight(420);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }

    @FXML
    private void handleSettingsMouseEntered(MouseEvent mouseEvent) //наведение мышки на кнопку настроек
    {fxSettings.setRotate(60);} //поворот на 60 градусов

    @FXML
    private void handleSettingsMouseExited(MouseEvent mouseEvent) //выход мышки с кнопки настроек
    {fxSettings.setRotate(0);} //возвращение в исходную позицию

    @Override
    public void initialize(URL url, ResourceBundle rb) {} //точка запуска проекта
}