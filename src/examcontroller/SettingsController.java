package examcontroller;

import examcontroller.animations.Shake;
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
    @FXML private TextField sName;
    @FXML private TextField sSurname;
    @FXML private TextField sPatronymic;
    @FXML private TextField sPhone;
    @FXML private TextField sMail;
    @FXML private TextField sLogin;
    @FXML private TextField sPassword;
    @FXML private PasswordField sNewPassword;
    @FXML private PasswordField sNewPassword2;
    @FXML private Label sErrMsg; 

    @FXML
    private void handleButtonCancel(ActionEvent event)
    {((Stage)((Node) event.getSource()).getScene().getWindow()).close();} //закрыть текущее окно
    
    @FXML
    private void handleButtonSave(ActionEvent event)
    {
        Shake errMsgAnim = new Shake(sErrMsg);
        if((sName.getText().equals(""))||(sSurname.getText().equals(""))||(sLogin.getText().equals("")))
        {
            sErrMsg.setText("не все поля заполнены");
            errMsgAnim.playAnim();
        }
        else
        {
            int isLoginExists = DatabaseHandler.isLoginExists(sLogin.getText());
            if((!sLogin.getText().equals(GLOBAL.user.getLogin()))&&(isLoginExists!=0)) //если было изменение логина и такой логин уже существует
            {
                
                if(isLoginExists == -1)
                {
                    sErrMsg.setText("не удалось подключиться к БД");
                    errMsgAnim.playAnim();
                }
                else
                {
                    sErrMsg.setText("аккаунт с таким логином уже существует");
                    errMsgAnim.playAnim();
                }
            }
            else if(sPassword.getText().equals(""))
            {
                sErrMsg.setText("поле 'текущий пароль' должно быть заполненным");
                errMsgAnim.playAnim();
            }
            else
            {
                int a = DatabaseHandler.checkPassword(GLOBAL.user.getId(), sPassword.getText()); //проверка, правильно ли введён текущий пароль
                switch(a)
                {
                    case -1:
                        sErrMsg.setText("не удается подключиться к БД");
                        errMsgAnim.playAnim();
                        break;

                    case 0:
                        sErrMsg.setText("неправильно введён текущий пароль");
                        errMsgAnim.playAnim();
                        break;

                    default:
                        if(!sNewPassword.getText().equals("")) //при попытке смены пароля
                        {
                            if(sNewPassword.getText().length()<8)
                            {
                                sErrMsg.setText("пароль должен состоять минимум из 8 символов");
                                errMsgAnim.playAnim();
                            }
                            else if(sNewPassword.getText().equals(sNewPassword2.getText()))
                            {
                                int c = DatabaseHandler.updateUser(GLOBAL.user.getId(), sLogin.getText(), sNewPassword.getText(),
                                        GLOBAL.user.getType(), sSurname.getText(), sName.getText(),
                                        sPatronymic.getText(), sPhone.getText(), sMail.getText());
                                if (c==0)
                                {
                                    sErrMsg.setText("ошибка при выполнении запроса");
                                    errMsgAnim.playAnim();
                                }
                                else ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
                            }
                            else
                            {
                                sErrMsg.setText("'новый пароль' и 'подтверждение' должны совпадать");
                                errMsgAnim.playAnim();
                            }
                        }
                        else //если меняется только информация профиля (без смены пароля)
                        {
                            int c = DatabaseHandler.updateUser(GLOBAL.user.getId(), sLogin.getText(), sPassword.getText(),
                                    GLOBAL.user.getType(), sSurname.getText(), sName.getText(),
                                    sPatronymic.getText(), sPhone.getText(), sMail.getText());
                            if (c==0)
                            {
                                sErrMsg.setText("ошибка при выполнении запроса");
                                errMsgAnim.playAnim();
                            }
                            else ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
                        }
                        break;
                }
            }             
        }
    }

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
