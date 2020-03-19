import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class UserLoginDataController implements Initializable
{
    @FXML private Label login;
    @FXML private Label password;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        login.setText(GLOBAL.TEMP_LOGIN);
        password.setText(GLOBAL.TEMP_PASSWORD);
    }

    @FXML
    private void handleButtonOk(ActionEvent event) //кнопка "Ok"
    {
        ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
        GLOBAL.TEMP_LOGIN = "";
        GLOBAL.TEMP_PASSWORD = "";
    }
}
