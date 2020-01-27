package examcontroller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController implements Initializable
{
    @FXML
    private TextField sName;
    @FXML
    private TextField sSurname;
    @FXML
    private TextField sPatronymic;
    @FXML
    private TextField sPhone;
    @FXML
    private TextField sMail;
    @FXML
    private TextField sLogin;
    @FXML
    private TextField sPassword;
    @FXML
    private PasswordField sNewPassword;
    @FXML
    private Label sErrMsg; 

    @FXML
    private void handleButtonCancel(ActionEvent event)
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        sSurname.setText(GLOBAL.user.getSurname());
        sName.setText(GLOBAL.user.getName());
        sPatronymic.setText(GLOBAL.user.getPatronymic());
        sPhone.setText(GLOBAL.user.getPhone());
        sMail.setText(GLOBAL.user.getMail());
        sLogin.setText(GLOBAL.user.getLogin());
        sPassword.requestFocus();
    }    
}
