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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DBConfigController implements Initializable
{
    @FXML private TextField fxURL;
    @FXML private TextField fxPort;
    @FXML private TextField fxDBName;
    @FXML private TextField fxUser;
    @FXML private PasswordField fxPassword;
    @FXML private Label fxErrMsg;

    @FXML
    private void handleButtonCancel(ActionEvent actionEvent) //кнопка "отмена"
    {((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close();} //закрыть текущее окно

    @FXML
    private void handleButtonSave(ActionEvent actionEvent) //кнопка "сохранить"
    {
        Shake errMsgAnim = new Shake(fxErrMsg);
        if (fxURL.getText().equals("") || fxPort.getText().equals("") || fxDBName.getText().equals("") || fxUser.getText().equals("") || fxPassword.getText().equals(""))
            fxErrMsg.setText(GLOBAL.ERROR_EMPTY_FIELDS);
        else
        {
            try
            {
                FileWriter writer = new FileWriter(GLOBAL.DBCONFIGURL);
                writer.write(fxURL.getText() + "\n" +
                        fxPort.getText() + "\n" +
                        fxDBName.getText() + "\n" +
                        fxUser.getText() + "\n" +
                        fxPassword.getText());
                writer.flush();
                ((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
            }
            catch (IOException e)
            {
                fxErrMsg.setText(GLOBAL.ERROR_UNSUCCESSFUL_SAVE);
                e.printStackTrace();
            }
        }
        errMsgAnim.playAnim();
    }

    @FXML
    private void handleCreateLabel(MouseEvent event) //нажатие на фразу "мне нужно создать базу данных"
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AllUserActions.class.getResource("/DBCreate.fxml"));
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setTitle(GLOBAL.TITLE + " - создание БД");
        stage.setMinWidth(560);
        stage.setMinHeight(420);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();

        ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            FileReader fr = new FileReader(GLOBAL.DBCONFIGURL);
            BufferedReader reader = new BufferedReader(fr);
            fxURL.setText(reader.readLine());
            fxPort.setText(reader.readLine());
            fxDBName.setText(reader.readLine());
            fxUser.setText(reader.readLine());
            fxPassword.setText(reader.readLine());
        } catch (IOException e) {e.printStackTrace();}

        //если в файле конфигурации оказалось недостаточно данных
        if (fxURL.getText() == null) fxURL.setText("");
        if (fxPort.getText() == null) fxPort.setText("");
        if (fxDBName.getText() == null) fxDBName.setText("");
        if (fxUser.getText() == null) fxUser.setText("");
        if (fxPassword.getText() == null) fxPassword.setText("");
    }
}
