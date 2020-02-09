import animations.Shake;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStudentController implements Initializable
{
    @FXML private TextField sSurname;
    @FXML private TextField sName;
    @FXML private TextField sPatronymic;
    @FXML private TextField sPhone;
    @FXML private TextField sMail;
    @FXML private ComboBox<Group> sGroup;
    @FXML private Label sErrMsg;
    
    @FXML
    private void handleButtonCancel(ActionEvent event) //кнопка "Отмена"
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @FXML
    private void handleButtonCreate(ActionEvent event) //кнопка "Создать"
    {
        Shake errMsgAnim = new Shake(sErrMsg);
        DatabaseHandler dbHandler = new DatabaseHandler();
        if((sName.getText().equals(""))||(sSurname.getText().equals("")))
        {
            sErrMsg.setText("не все поля заполнены");
            errMsgAnim.playAnim();
        }
        else if (sGroup.getValue()==null)
        {
            sErrMsg.setText("не выбрана группа");
            errMsgAnim.playAnim();
        }
        else
        {
            dbHandler.createStudent(sSurname.getText(), sName.getText(),
                    sPatronymic.getText(), sGroup.getValue().getId(),
                    sPhone.getText(), sMail.getText());
            ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        sGroup.setItems(FXCollections.observableArrayList(new DatabaseHandler().getGroups())); //получение списка групп
    }
}
