import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DBCreationSucceedController implements Initializable
{
    @FXML
    public void handleButtonOK(ActionEvent event) //кнопка "ОК"
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}