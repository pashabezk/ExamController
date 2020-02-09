import TableLists.UsersTableList;
import animations.Shake;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddExamController implements Initializable
{
    @FXML private Label sErrMsg;
    @FXML private TextField fxSubject;
    @FXML private ComboBox<UsersTableList> fxTeacher;
    @FXML private ComboBox<Group> fxGroup;
    @FXML private DatePicker fxDate;
    @FXML private ComboBox<ExamType> fxType;

    @FXML
    private void handleButtonCancel(ActionEvent event) //кнопка "Отмена"
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @FXML
    private void handleButtonCreate(ActionEvent event) //кнопка "Создать"
    {
        Shake errMsgAnim = new Shake(sErrMsg);
        if(fxSubject.getText().equals(""))
        {
            sErrMsg.setText("не заполнено поле \"Предмет\"");
            errMsgAnim.playAnim();
        }
        else if(fxTeacher.getValue()==null)
        {
            sErrMsg.setText("не выбран преподаватель");
            errMsgAnim.playAnim();
        }
        else if(fxGroup.getValue()==null)
        {
            sErrMsg.setText("не выбрана группа");
            errMsgAnim.playAnim();
        }
        else if(fxDate.getValue()==null)
        {
            sErrMsg.setText("не выбрана дата");
            errMsgAnim.playAnim();
        }
        else
        {
            new DatabaseHandler().createExam(fxSubject.getText(),
                    fxType.getValue().getCode(), fxTeacher.getValue().getId(),
                    fxGroup.getValue().getId(), fxDate.getValue().toString());
            ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
        }
    }

    public class ExamType
    {
        public int code;
        private String name;

        public ExamType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {return code;}
        public String getName() {return name;}

        @Override
        public String toString() {return this.name;}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        DatabaseHandler db = new DatabaseHandler();
        fxTeacher.setItems(FXCollections.observableArrayList(db.getUsers())); //получение списка преподавателей
        fxGroup.setItems(FXCollections.observableArrayList(db.getGroups())); //получение списка групп
        fxType.setItems(FXCollections.observableArrayList(new ExamType(1, "Недифференцированный"), new ExamType(2, "Дифференцированный"))); //установка значений "тип экзамена"
        fxType.getSelectionModel().selectLast(); //установка значения "Дифференцированный" по умолчанию
    }
}
