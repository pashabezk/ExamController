import animations.Shake;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        int isLoginExists = DatabaseHandler.isLoginExists(sLogin.getText());
        if((sName.getText().equals(""))||(sSurname.getText().equals(""))||(sLogin.getText().equals("")))
        {
            sErrMsg.setText(GLOBAL.ERROR_EMPTY_FIELDS);
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
                    sErrMsg.setText(GLOBAL.ERROR_DB_CONNECTION);
                    errMsgAnim.playAnim();
                    break;

                case 0:
                    int paswrd = (int) (Math.random() * 900000) + 99999;
                    sErrMsg.setText(""); //убрать лейбл с выводом ошибок
                    DatabaseHandler.createUser(sLogin.getText(), paswrd+"", sUserType.getValue().getCode(),
                            sSurname.getText(), sName.getText(), sPatronymic.getText(),
                            sPhone.getText(), sMail.getText());
                    ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно

                    FXMLLoader loader = new FXMLLoader();
                    GLOBAL.TEMP_LOGIN = sLogin.getText();
                    GLOBAL.TEMP_PASSWORD = ""+paswrd;
                    loader.setLocation(getClass().getResource("/UserLoginData.fxml"));
                    try{
                        loader.load();
                    } catch (IOException ex) {ex.printStackTrace();}

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Пользователь создан");
                    stage.setMinWidth(300);
                    stage.setMinHeight(200);
                    stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
                    stage.show();
                    break;

                default:
                    sErrMsg.setText("аккаунт с таким логином уже существует");
                    errMsgAnim.playAnim();
                    break;
            }
        }
    }

    private class UserType
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

