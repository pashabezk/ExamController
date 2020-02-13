import animations.Shake;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AuthorizationController implements Initializable
{
    @FXML private TextField tfLogin;
    @FXML private TextField tfPassword;
    @FXML private Label lbMsgAuth;

    @FXML
    private void handleButtonAauthorization(ActionEvent event)
    {
        Shake errMsgAnim = new Shake(lbMsgAuth);

        if((tfPassword.getText().equals(""))||(tfLogin.getText().equals("")))
        {
            lbMsgAuth.setText("не все поля заполнены");
            errMsgAnim.playAnim();
        }
        else
        {
            int userID = DatabaseHandler.signIn(tfLogin.getText(), tfPassword.getText());

            switch (userID)
            {
                case -1:
                    lbMsgAuth.setText("не удалось подключиться к БД");
                    errMsgAnim.playAnim();
                    break;

                case 0:
                    lbMsgAuth.setText("неправильный логин или пароль");
                    errMsgAnim.playAnim();
                    break;

                default:
                    lbMsgAuth.setText(""); //убрать лейбл с выводом ошибок
                    ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть окно аторизации

                    GLOBAL.user = DatabaseHandler.getUserByID(userID); //получение параметров авторизовавшегося пользователя

                    FXMLLoader loader = new FXMLLoader();
                    switch (GLOBAL.user.getType()) //загрузка разных страниц в зависимости, от типа пользователя
                    {
//                        case 1: loader.setLocation(getClass().getResource("/DMainWindow.fxml")); break;
//                        case 2: loader.setLocation(getClass().getResource("/TMainWindow.fxml")); break;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}