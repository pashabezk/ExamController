import TableLists.ExamTableList;
import TableLists.StudentTableList;
import TableLists.UsersTableList;
import animations.Shake;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AddMarkController implements Initializable
{
    @FXML private ComboBox<ExamTableList> fxExam;
    @FXML private ComboBox<UsersTableList> fxTeacher;
    @FXML private ComboBox<StudentTableList> fxStudent;
    @FXML private ComboBox<Integer> fxMark;
    @FXML private DatePicker fxDate;
    @FXML private Label sErrMsg;

    @FXML
    private void handleButtonCancel(ActionEvent event) //кнопка "Отмена"
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @FXML
    private void handleButtonCreate(ActionEvent event) //кнопка "Создать"
    {
        Shake errMsgAnim = new Shake(sErrMsg);
        if (fxExam.getValue()==null) sErrMsg.setText("не выбран экзамен");
        else if (fxTeacher.getValue()==null) sErrMsg.setText("не указан преподаватель");
        else if (fxStudent.getValue()==null) sErrMsg.setText("не выбран студент");
        else if (fxMark.getValue()==null) sErrMsg.setText("не указана оценка");
        else if (fxDate.getValue()==null) sErrMsg.setText("не указана дата");
        else
        {
            int retake = DatabaseHandler.getNumberOfRetakes(fxStudent.getValue().getId(), fxExam.getValue().getId());
            if(retake >= 3) sErrMsg.setText("у этого студента уже есть три сдачи");
            else if(retake>=0) //если сдач было меньше трёх или вообще не было
            {
                DatabaseHandler.createMark(fxExam.getValue().getId(), fxStudent.getValue().getId(),
                        fxTeacher.getValue().getId(), fxMark.getValue(), retake+1);
                ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
            }
            else sErrMsg.setText("не удалось подключиться к БД");
        }
        errMsgAnim.playAnim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        fxExam.setItems(FXCollections.observableArrayList(DatabaseHandler.getExams())); //получение списка экзаменов
        fxTeacher.setItems(FXCollections.observableArrayList(DatabaseHandler.getUsers())); //получение списка преподавателей
        fxStudent.setItems(FXCollections.observableArrayList(DatabaseHandler.getStudents())); //получение списка студентов

        fxExam.valueProperty().addListener(new ChangeListener<ExamTableList>() //установка прослушивателя на выбор экзамена, чтобы заполнять список оценок
        {
            @Override
            public void changed(ObservableValue<? extends ExamTableList> observable, ExamTableList oldValue, ExamTableList newValue)
            {
                if(newValue.getMarkStSTR().equals(ExamTableList.MARK_ST_DIFF)) //если зачёт дифференцированный
                    fxMark.setItems(FXCollections.observableArrayList(2, 3, 4, 5)); //создание списка оценок: 2, 3, 4, 5
                else fxMark.setItems(FXCollections.observableArrayList(0, 1)); //зачёт недифференцированный: 0 - незач, 1 - зачёт
            }
        });
    }
}
