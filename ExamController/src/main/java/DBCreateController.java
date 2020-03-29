import animations.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DBCreateController implements Initializable
{
    @FXML private AnchorPane fxStepOne;
    @FXML private AnchorPane fxStepTwo;
    @FXML private TextField fxURL;
    @FXML private TextField fxPort;
    @FXML private TextField fxDBName;
    @FXML private TextField fxUser;
    @FXML private PasswordField fxPassword;
    @FXML private Label fxErrMsg;

    @FXML
    private void handleButtonCancel(ActionEvent event) //кнопка "Отмена"
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AllUserActions.class.getResource("/DBConfig.fxml"));
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setTitle(GLOBAL.TITLE + " - конфигурация БД");
        stage.setMinWidth(560);
        stage.setMinHeight(420);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();

        ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
    }

    @FXML
    private void handleButtonNext(ActionEvent event) //кнопка "Далее"
    {
        fxStepOne.setVisible(false); //скрытие шага 1
        fxStepTwo.setVisible(true); //отображение шага 2
    }

    @FXML
    private void handleButtonBack(ActionEvent event) //кнопка "Назад"
    {
        fxStepOne.setVisible(true); //отображение шага 1
        fxStepTwo.setVisible(false); //скрытие шага 2
    }

    @FXML
    private void handleButtonCreate(ActionEvent aevent) //кнопка "Создать"
    {
        Shake errMsgAnim = new Shake(fxErrMsg);
        if (fxURL.getText().equals("") || fxPort.getText().equals("") || fxDBName.getText().equals("") || fxUser.getText().equals("") || fxPassword.getText().equals(""))
            fxErrMsg.setText(GLOBAL.ERROR_EMPTY_FIELDS);
        else
        {
            fxErrMsg.setText("Идёт создание БД. Это может занять какое-то время");
            DatabaseHandler.DBURL = fxURL.getText();
            DatabaseHandler.DBPORT = fxPort.getText();
            DatabaseHandler.DBNAME = ""; //пусто, т.к. БД ещё не создана
            DatabaseHandler.MYSQL_USER = fxUser.getText();
            DatabaseHandler.MYSQL_PASSWORD = fxPassword.getText();

            switch (DatabaseHandler.createDatabase(fxDBName.getText()))
            {
                case -1: fxErrMsg.setText(GLOBAL.ERROR_DB_CONNECTION); break;
                case 0: fxErrMsg.setText("не удалось создать БД"); break;
                case 1: //создание БД прошло успешно
                    try //попытка сохранить используемую конфигурацию
                    {
                        FileWriter writer = new FileWriter(GLOBAL.DBCONFIGURL);
                        writer.write(fxURL.getText() + "\n" +
                                fxPort.getText() + "\n" +
                                fxDBName.getText() + "\n" +
                                fxUser.getText() + "\n" +
                                fxPassword.getText());
                        writer.flush();
                    }
                    catch (IOException e) {e.printStackTrace();}

                    ((Stage)((Node) aevent.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
                    break;
            }
        }
        errMsgAnim.playAnim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}
