package examcontroller;

import examcontroller.animations.Shake;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddGroupController implements Initializable
{
    @FXML
    private TextField fxName;
    @FXML
    private ComboBox fxCourse;
    @FXML
    private ComboBox<Integer> fxYear;
    @FXML
    private Label sErrMsg;
    
    @FXML
    private void handleButtonCancel(ActionEvent event) //кнопка "Отмена"
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @FXML
    private void handleButtonCreate(ActionEvent event) //кнопка "Создать"
    {
        Shake errMsgAnim = new Shake(sErrMsg);
        DatabaseHandler dbHandler = new DatabaseHandler();
        if(fxName.getText().equals(""))
        {
            sErrMsg.setText("не заполнено имя группы");
            errMsgAnim.playAnim();
        }
        else
        {
            dbHandler.createGroup(fxName.getText(),
                    Integer.parseInt(fxCourse.getValue().toString()),
                    fxYear.getValue());
            ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        fxCourse.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        fxCourse.getSelectionModel().selectFirst(); //установка значения по умолчанию
        
        ObservableList list = FXCollections.observableArrayList();
        for (int i=Calendar.getInstance().get(Calendar.YEAR); i>1999; i--) //получение текущего года и создание списка
            list.add(i); //установка списка "год начала обучения"
        fxYear.setItems(list);
        fxYear.getSelectionModel().selectFirst(); //установка значения по умолчанию
    }
}
