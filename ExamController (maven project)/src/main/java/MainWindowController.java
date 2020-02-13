import TableLists.MarkTableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable
{
    @FXML private ListView<String> TMListView;
    @FXML private TableView<MarkTableList> tblMarks;

    private Exam examSelected; //поле, в котором содерижтся объект экзамена, для которого выводдятся оценки
    private ArrayList<Exam> exam; //список экзаменов преподавателя
    private MarkTableList markItem; //строчка в таблице, на которую было произведено нажатие

    @FXML
    private void handleButtonSettings(javafx.event.ActionEvent event) //кнопка "настройки"
    {AllUserActions.openSettingsWindow();}

    @FXML
    private void handleButtonExit(javafx.event.ActionEvent event) //кнопка "выход"
    {
        ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
        AllUserActions.exitAndAuth();
    }

    @FXML
    public void handleButtonRetake(ActionEvent actionEvent)
    {
        if(markItem.getRetake()<3) //если сдач экзамена меньше 3
        {
            DatabaseHandler.createMark(markItem.getExamID(), markItem.getStudentID(), GLOBAL.user.getId(), 0, markItem.getRetake()+1); //создание пересдачи

            initTableMarks(); //перерисовка таблицы с оценками
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Нельзя сдавать предмет больше трёх раз");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(ExamController.class.getResourceAsStream("/icon.png")));
            alert.showAndWait();
        }
        System.out.println(markItem.getId() + "");
    }

    @FXML
    public void handleButtonDelete(ActionEvent actionEvent)
    {
        if(markItem.getRetake()>1) //если сдач экзамена больше 1
        {
            DatabaseHandler.deleteMark(markItem.getId()); //удаление пересдачи
            initTableMarks(); //перерисовка таблицы с оценками
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Нельзя иметь меньше одной сдачи");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(ExamController.class.getResourceAsStream("/icon.png")));
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initExamList(); //отображения списка экзаменов
        initTableMarks(); //отображение таблицы с оценками
        tblMarks.setEditable(true); //разрешение редактирования таблицу
        tblMarks.setOnMousePressed(event -> markItem = tblMarks.getSelectionModel().getSelectedItem()); //установка прослушивателя на нажатие строчки в таблице, чтобы получать объект строки при нажатии

        TMListView.getSelectionModel().selectedItemProperty().addListener((changed, oldValue, newValue) -> //добавление прослушивателя на список экзаменов, чтобы обновлять таблицу оценок при выборе экзамена
        {
            examSelected = exam.get(exam.size()-1-TMListView.getSelectionModel().getSelectedIndex()); //получение нажатого экзамена
            initTableMarks(); //обновление таблицы с оценками
        });
    }

    private void initExamList() //отображения списка экзаменов в окне справа
    {
        ObservableList<String> list = FXCollections.observableArrayList(); //список, из которого будет происходить вывод на экран
        exam = DatabaseHandler.getExamsByUID(GLOBAL.user.getId()); //список экзаменов, откуда будет браться информация
        Group gr;

        for (int i = exam.size()-1; i >= 0; i--)
        {
            gr = DatabaseHandler.getGroupByID(exam.get(i).getGroup());
            list.add(gr.getName() + " " + exam.get(i).getName() + " (" + exam.get(i).getDate() + ")");
        }

        examSelected = exam.get(exam.size()-1); //при запуске таблицы отображаются оценки за последний экзамен
        TMListView.setItems(list);
    }

    private void initTableMarks() //отображение таблицы с оценками
    {
        TableColumn<MarkTableList, String> studentColumn = new TableColumn<>("Студент");
        TableColumn<MarkTableList, Integer> markColumn = new TableColumn<>("Оценка");
        TableColumn<MarkTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<MarkTableList, Integer> retakeColumn = new TableColumn<>("№ пересдачи");

        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        retakeColumn.setCellValueFactory(new PropertyValueFactory<>("retake"));

        ObservableList<Integer> markVarList = FXCollections.observableArrayList(); //создание списка оценок
        if(examSelected.getMark_st()==1) //если статус экзамена: зач/незач, то варианты будут 0-незачёт и 1-зачёт
        {
            markVarList.add(0);
            markVarList.add(1);
        } else { //иначе будут оценки 2,3,4,5
            markVarList.add(2);
            markVarList.add(3);
            markVarList.add(4);
            markVarList.add(5);
        }

        markColumn.setCellFactory(ComboBoxTableCell.forTableColumn(markVarList));
        markColumn.setOnEditCommit(event ->
        {
            DatabaseHandler.updateMark(event.getRowValue().getId(), event.getNewValue()); //внесение исправленной оценки в базу данных
            initTableMarks(); //перерисовка таблицы с оценками
        });

        tblMarks.setItems(FXCollections.observableArrayList(DatabaseHandler.getMarksByEID(examSelected.getId()))); //добавление информации в таблицу
        if(tblMarks.getColumns().isEmpty()) //если колонок ещё нет, то добавить их
            tblMarks.getColumns().addAll(studentColumn, markColumn, dateColumn, retakeColumn);
    }
}
