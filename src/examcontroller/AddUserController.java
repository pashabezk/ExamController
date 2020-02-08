package examcontroller;

import examcontroller.animations.Shake;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AddUserController implements Initializable
{
    @FXML private TextField sName;
    @FXML private TextField sSurname;
    @FXML private TextField sPatronymic;
    @FXML private TextField sPhone;
    @FXML private TextField sMail;
    @FXML private TextField sLogin;
    @FXML private Label sErrMsg;    
    @FXML private ComboBox<UserType> sUserType;
    
    @FXML
    private void handleButtonCancel(ActionEvent event) //кнопка "Отмена"
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно
    
    @FXML
    private void handleButtonCreate(ActionEvent event) //кнопка "Создать"
    {
        Shake errMsgAnim = new Shake(sErrMsg);
        DatabaseHandler dbHandler = new DatabaseHandler();
        int isLoginExists = dbHandler.isLoginExists(sLogin.getText());
        if((sName.getText().equals(""))||(sSurname.getText().equals(""))||(sLogin.getText().equals("")))
        {
            sErrMsg.setText("не все поля заполнены");
            errMsgAnim.playAnim();
        }
        else if (sUserType.getValue()==null)
        {
            sErrMsg.setText("не выбран тип пользователя");
            errMsgAnim.playAnim();
        }
        else
        {
            switch (isLoginExists)
            {
                case -1:
                    sErrMsg.setText("не удалось подключиться к БД");
                    errMsgAnim.playAnim();
                    break;

                case 0:
                    int paswrd = (int) (Math.random() * 900000) + 99999;
                    sErrMsg.setText(""); //убрать лейбл с выводом ошибок
                    dbHandler.createUser(sLogin.getText(), paswrd+"", sUserType.getValue().getCode(),
                            sSurname.getText(), sName.getText(), sPatronymic.getText(),
                            sPhone.getText(), sMail.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Пользователь создан");                                
                    alert.setHeaderText(null);
                    alert.setContentText("Данные для входа:\nЛогин: " + 
                            sLogin.getText()+"\nПароль: " + paswrd);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
                    alert.showAndWait();
                    ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
                    break;

                default:
                    sErrMsg.setText("аккаунт с таким логином уже существует");
                    errMsgAnim.playAnim();
                    break;
            }
        }
    }
    
    public class UserType
    {
        public int code;
        private String name;

        public UserType(int code, String name) {
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
        sUserType.setItems(FXCollections.observableArrayList(new UserType(1, "Сотрудник администрации"), new UserType(2, "Преподаватель"), new UserType(3, "Администратор БД")));
        
        sSurname.textProperty().addListener(new ChangeListener<String>() { //установка прослушивателя на ввод фамилии для автоматической генерации логина пользователя
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
                sLogin.setText(Translit.cyr2lat(sSurname.getText()+sName.getText()));
            }
        });
        
        sName.textProperty().addListener(new ChangeListener<String>() { //установка прослушивателя на ввод имени для автоматической генерации логина пользователя
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
                sLogin.setText(Translit.cyr2lat(sSurname.getText()+sName.getText()));
            }
        });
    }
}
