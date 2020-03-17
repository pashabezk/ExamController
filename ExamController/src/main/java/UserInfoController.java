import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoController implements Initializable {

    @FXML private Button okBtn;
    @FXML private Label login;
    @FXML private Label password;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        login.setText(GLOBAL.temp_login);
        password.setText(GLOBAL.temp_password);
    }

    @FXML
    private void handleButtonOk(ActionEvent event) //кнопка "Ok"
    {
        ((Stage)((Node) event.getSource()).getScene().getWindow()).close();
        GLOBAL.temp_login = "";
        GLOBAL.temp_password = "";
    } //закрыть текущее окно
}
