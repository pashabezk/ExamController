import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DMainWindowController implements Initializable
{
    @FXML private TableView<examTableList> tblMarks;
    @FXML private ButtonBar btnTMSettings;
    @FXML private Button btnTMExit;
    @FXML private ListView<String> DMListView;
    
    public Exam examSelected; //поле, в котором содерижтся объект экзамена, для которого выводдятся оценки
    public ArrayList<Exam> exam; //список экзаменов преподавателя
    public ObservableList<Mark> markList; //список оценок
    
    @FXML
    private void handleButtonSettings(javafx.event.ActionEvent event) //кнопка "настройки"
    {AllUserActions.openSettingsWindow(); }

    @FXML
    private void handleButtonExit(javafx.event.ActionEvent event) //кнопка "выход"
    {
        ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно
        AllUserActions.exitAndAuth();
    }

    @FXML
    private void handleButtonMonitor(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MonitoringWindow.fxml"));
        try{
            loader.load();
        } catch (IOException ex) {ex.printStackTrace();}

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(GLOBAL.TITLE + " - мониторинг деятельности");
        stage.setMinWidth(560);
        stage.setMinHeight(400);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }
    
    public class examTableList //класс отображения таблицы
    {        
        private int id; //илентификатор оценки
        private int stId; //илентификатор студента
        private String student;
        private int mark;
        private String date;
        private int retake;

        public examTableList(int id, int stId, String student, int mark, String date, int retake)
        {
            this.id = id;
            this.stId = stId;
            this.student = student;
            this.mark = mark;
            this.date = date;
            this.retake = retake;
        }
        
        public examTableList(int stId, String student)
        {
            id = 0;
            this.stId = stId;
            this.student = student;
            mark = 0;
            date = "";
            retake = 0;
        }
        
        public int getId() {return id;}
        public int getStId() {return stId;}
        public String getStudent() {return student;}
        public int getMark() {return mark;}
        public String getDate() {return date;}
        public int getRetake() {return retake;}

        public void setMark(int mark) {this.mark = mark;}
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initExamList(); //отображения списка экзаменов
        initTableMarks(); //отображение таблицы с оценками
        tblMarks.setEditable(true); //разрешение редактирования таблицы
        
        DMListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() //добавление прослушивателя на список экзаменов, чтобы обновлять таблицу оценок при выборе экзамена
        {             
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
                examSelected = exam.get(exam.size()-1-DMListView.getSelectionModel().getSelectedIndex()); //получение нажатого экзамена
                initTableMarks(); //обновление таблицы с оценками
            }
        });
    }
    
    public void initExamList() //отображения списка экзаменов в окне справа
    {
        ObservableList<String> list = FXCollections.observableArrayList(); //список, из которого будет происходить вывод на экран        
        exam = new DatabaseHandler().getExamsByUID(GLOBAL.user.getId()); //список экзаменов, откуда будет браться информация
        Group gr;
        
        for (int i = exam.size()-1; i >= 0; i--)
        {
           gr = new DatabaseHandler().getGroupByID(exam.get(i).getGroup());
           list.add(gr.getName() + " " + exam.get(i).getName() + " (" + exam.get(i).getDate() + ")");            
        }
        
        examSelected = exam.get(exam.size()-1); //при запуске таблицы отображаются оценки за последний экзамен
        DMListView.setItems(list);
    }    
    
    public void initTableMarks() //отображение таблицы с оценками
    {
        TableColumn<DMainWindowController.examTableList, String> studentColumn = new TableColumn<>("Студент");
        TableColumn<DMainWindowController.examTableList, Integer> markColumn = new TableColumn<>("Оценка");
        TableColumn<DMainWindowController.examTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<DMainWindowController.examTableList, Integer> retakeColumn = new TableColumn<>("№ пересдачи");
        TableColumn<DMainWindowController.examTableList, Void> retakeBtnColumn = new TableColumn("");
        TableColumn<DMainWindowController.examTableList, Void> deleteBtnColumn = new TableColumn("");
        
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
        markColumn.setOnEditCommit((TableColumn.CellEditEvent<DMainWindowController.examTableList, Integer> event) -> //прослушиватель на изменение оценки
        {
            Mark m = markList.get(event.getTablePosition().getRow()); //получение оценки
            m.setMark(event.getNewValue()); //запись в оценку нового результата
            DatabaseHandler dh = new DatabaseHandler();
            dh.updateMark(m); //внесение исправленной оценки в базу данных
            initTableMarks(); //перерисовка таблицы с оценками
        });
        
        Callback<TableColumn<DMainWindowController.examTableList, Void>, TableCell<DMainWindowController.examTableList, Void>> cellFactoryRetake = new CallbackImplBtnRetake();
        retakeBtnColumn.setCellFactory(cellFactoryRetake); //добавление кнопок "пересдача"
        
        Callback<TableColumn<DMainWindowController.examTableList, Void>, TableCell<DMainWindowController.examTableList, Void>> cellFactoryDelete = new CallbackImplBtnDelete();
        deleteBtnColumn.setCellFactory(cellFactoryDelete); //добавление кнопок "удалить"
        
        ObservableList<DMainWindowController.examTableList> list = getMarkTableList();
        tblMarks.setItems(list);
        if(tblMarks.getColumns().isEmpty()) //если колонок ещё нет, то добавить их
            tblMarks.getColumns().addAll(studentColumn, markColumn, dateColumn, retakeColumn, retakeBtnColumn, deleteBtnColumn);
    }
    
    public ObservableList<DMainWindowController.examTableList> getMarkTableList() //возвращает список студентов группы, у которой проходит экхамен вместе с имеющимися оценками
    {
        ObservableList<DMainWindowController.examTableList> list = FXCollections.observableArrayList();
        markList = new DatabaseHandler().getMarksByEID(examSelected.getId()); //получение списка оценок за экзамен
        ArrayList<Student> stList= new DatabaseHandler().getStudentsByGrID(examSelected.getGroup()); //получение списка студентов группы, у которой проводится экзамен
        
        int id, i, j, isFound;
        for (i = 0; i< stList.size(); i++)
        {
            isFound = 0; //если было совпадение, то значение устанавливается в 1
            id = stList.get(i).getId();            
            for (j = 0; j< markList.size(); j++)
            {
                if(id == markList.get(j).getStudentID())
                {
                    isFound++;
                    list.add(new DMainWindowController.examTableList(markList.get(j).getId(), id,
                        stList.get(i).getFullName(), markList.get(j).getMark(),
                        markList.get(j).getDate(), markList.get(j).getRetake()));
                }
            }
            j = 0;
            while (isFound>1) //если у студента были пересдачи, то необходимо отображать последнюю оценку
            {
                if(list.get(j).getStId() == id)
                {
                    list.remove(j);
                    isFound--;
                    j--;
                }
                j++;
            }
            
            if(isFound == 0) //если оценки студента нет
                list.add(new DMainWindowController.examTableList(id, stList.get(i).getFullName()));
        }
        return list;
    }
    
    private class CallbackImplBtnRetake implements Callback<TableColumn<DMainWindowController.examTableList, Void>, TableCell<DMainWindowController.examTableList, Void>> //работа с кнопкой "пересдача"
    {
        public CallbackImplBtnRetake() {}

        @Override
        public TableCell<DMainWindowController.examTableList, Void> call(final TableColumn<DMainWindowController.examTableList, Void> param)
        {
            final TableCell<DMainWindowController.examTableList, Void> cell = new TableCell<DMainWindowController.examTableList, Void>()
            {                
                @Override
                public void updateItem(Void item, boolean empty) {
                    Button btn = new Button("Пересдача");                    
                    btn.setOnAction(new EventHandler<javafx.event.ActionEvent>()
                    {
                        @Override
                        public void handle(javafx.event.ActionEvent event)
                        {
                            DMainWindowController.examTableList data = getTableView().getItems().get(getIndex()); //получение строчки на которую было произведено нажатие                          
                            if(data.getRetake()<3) //если сдач экзамена меньше 3
                            {
                                DatabaseHandler dh = new DatabaseHandler();
                                dh.createMark(examSelected.getId(), data.getStId(), GLOBAL.user.getId(), 0, data.getRetake()+1); //создание пересдачи
                                initTableMarks(); //перерисовка таблицы с оценками
                            }
                            else
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");                                
                                alert.setHeaderText(null);
                                alert.setContentText("Нельзя сдавать предмет больше трёх раз");
                                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
                                alert.showAndWait();
                            }
                        }
                    });
                    
                    super.updateItem(item, empty);
                    if (empty) {setGraphic(null);}
                    else {setGraphic(btn);}
                }
            };
            return cell;
        }
    }
    
    private class CallbackImplBtnDelete implements Callback<TableColumn<DMainWindowController.examTableList, Void>, TableCell<DMainWindowController.examTableList, Void>> //работа с кнопкой "удалить"
    {
        public CallbackImplBtnDelete() {}

        @Override
        public TableCell<DMainWindowController.examTableList, Void> call(final TableColumn<DMainWindowController.examTableList, Void> param)
        {
            final TableCell<DMainWindowController.examTableList, Void> cell = new TableCell<DMainWindowController.examTableList, Void>()
            {                
                @Override
                public void updateItem(Void item, boolean empty) {
                    Button btn = new Button("Удалить");                    
                    btn.setOnAction(new EventHandler<javafx.event.ActionEvent>()
                    {
                        @Override
                        public void handle(javafx.event.ActionEvent event)
                        {
                            DMainWindowController.examTableList data = getTableView().getItems().get(getIndex()); //получение строчки на которую было произведено нажатие                      
                            if(data.getRetake()>1) //если сдач экзамена больше 1
                            {
                                DatabaseHandler dh = new DatabaseHandler();
                                dh.deleteMark(data.getId()); //удаление пересдачи
                                initTableMarks(); //перерисовка таблицы с оценками
                            }
                            else
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");                                
                                alert.setHeaderText(null);
                                alert.setContentText("Нельзя иметь меньше одной сдачи");
                                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                stage.getIcons().add(new Image(ExamController.class.getResourceAsStream("resources/icon.png")));
                                alert.showAndWait();
                            }
                        }
                    });
                    
                    super.updateItem(item, empty);
                    if (empty) {setGraphic(null);}
                    else {setGraphic(btn);}
                }
            };
            return cell;
        }
    }
}