package examcontroller;

import examcontroller.TableLists.ExamTableList;
import examcontroller.TableLists.MarkTableList;
import examcontroller.TableLists.StudentTableList;
import examcontroller.TableLists.UsersTableList;
import examcontroller.animations.Attenuation;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AMainWindowController implements Initializable
{
    @FXML
    private ListView<String> AMListView;
    @FXML
    private TableView AMTable;
    @FXML
    private Label labelPushUp;
    
    private int selectedItemId = 0;
    private String selectdedTableName = "Пользователи";
    
    @FXML
    private void handleButtonSettings(ActionEvent event)
    {
        AllUserActions a = new AllUserActions();
        a.openSettingsWindow();
    }

    @FXML
    private void handleButtonExit(ActionEvent event)
    {
        ((Stage)((Node) event.getSource()).getScene().getWindow()).close(); //закрыть текущее окно        
        AllUserActions a = new AllUserActions();
        a.exit();
    }
    
    @FXML
    private void handleButtonAdd(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        switch(selectdedTableName) //выбор открытия окна в зависимости от выбранного элемента списка
        {
            default:
                loader.setLocation(getClass().getResource("AddUser.fxml"));
                stage.setTitle(GLOBAL.TITLE + " - создание пользователя");
                break;
        }           
        try{ loader.load();
        } catch (IOException ex) {ex.printStackTrace();}
        Parent root = loader.getRoot();
        stage.setScene(new Scene(root));
        stage.setMinWidth(560);
        stage.setMinHeight(400);
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.show();
    }    
    
    @FXML
    private void handleButtonDelete(ActionEvent event)
    {
        Attenuation pushUp = new Attenuation(labelPushUp); //добавление эффекта затухания
        DatabaseHandler dbHandler = new DatabaseHandler();
        labelPushUp.setTextFill(Color.web("#ff0000"));
        
        switch(dbHandler.isUserCanBeDeleted(selectedItemId))
        {
            case 1: //в случае, если пользователя не привязан к экзаменам или оценкам
                switch(selectdedTableName) //удаление в зависимости от выбранного элемента списка
                {
                    default:
                        if (dbHandler.deleteUser(selectedItemId) == 1)
                        {
                            labelPushUp.setText("Запись удалёна");
                            labelPushUp.setTextFill(Color.web("#0000ff"));
                            initTableUsers(); //перерисовка таблицы
                        }
                        else labelPushUp.setText("ОШИБКА: удалить запись не удалось");
                        break;
                }
                break;
            case -1: labelPushUp.setText("ОШИБКА: не удалось подключиться к БД"); break;
            default: labelPushUp.setText("ОШИБКА: к этому пользователю привязаны экзамены"); break;
        }
        pushUp.playAnim();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ObservableList<String> list = FXCollections.observableArrayList(); //список, из которого будет происходить вывод на экран
        list.add("Пользователи");
        list.add("Студенты");
        list.add("Группы");
        list.add("Экзамены");
        list.add("Оценки");
        AMListView.setItems(list);
        
        initTableUsers();
        
        AMListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() //добавление прослушивателя на список экзаменов, чтобы обновлять таблицу оценок при выборе экзамена
        {             
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) //установка прослушателя на список в левой колонке
            {
                selectdedTableName = newValue;
                switch(newValue) //инициализация столбцов таблицы в зависимости от выбранного пункта
                {
                    case "Пользователи": initTableUsers(); break;
                    case "Студенты": initTableStudents(); break;
                    case "Группы": initTableGroups(); break;
                    case "Экзамены": initTableExams(); break;
                    case "Оценки": initTableMarks(); break;
                }
            }
        });
        
        AMTable.setOnMousePressed(new EventHandler<MouseEvent>() //установка прослушивателя на нажатие строчки в таблице
        {
            @Override 
            public void handle(MouseEvent event) {
                switch(selectdedTableName) //получение идентификатора нажатого объекта
                {
                    case "Пользователи": selectedItemId = ((UsersTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Студенты": selectedItemId = ((StudentTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Группы": selectedItemId = ((Group) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Экзамены": selectedItemId = ((ExamTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Оценки": selectedItemId = ((MarkTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                }
            }
        });
    }    

    public void initTableUsers()
    {
        AMTable.getColumns().clear(); //очистка таблицы
        TableColumn<UsersTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<UsersTableList, String> surnameColumn = new TableColumn<>("Фамилия");
        TableColumn<UsersTableList, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<UsersTableList, String> patronymicColumn = new TableColumn<>("Отчество");
        TableColumn<UsersTableList, String> typeColumn = new TableColumn<>("Тип пользователя");
        TableColumn<UsersTableList, String> phoneColumn = new TableColumn<>("Телефон");
        TableColumn<UsersTableList, String> mailColumn = new TableColumn<>("Почта");
        TableColumn<UsersTableList, Void> btnColumn = new TableColumn("Пароль");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));               
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeSTR"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        
        Callback<TableColumn<UsersTableList, Void>, TableCell<UsersTableList, Void>> cellFactoryRestorePassword = new CallbackImplRestorePassword();
        btnColumn.setCellFactory(cellFactoryRestorePassword); //добавление кнопок "пересдача"

        AMTable.setItems(FXCollections.observableArrayList(new DatabaseHandler().getUsers())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, surnameColumn, nameColumn, patronymicColumn, typeColumn, phoneColumn, mailColumn, btnColumn); //добавление колонок
    }

    private void initTableStudents()
    {
        AMTable.getColumns().clear(); //очистка таблицы
        TableColumn<UsersTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<StudentTableList, String> grouppColumn = new TableColumn<>("Группа");
        TableColumn<StudentTableList, String> surnameColumn = new TableColumn<>("Фамилия");
        TableColumn<StudentTableList, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<StudentTableList, String> patronymicColumn = new TableColumn<>("Отчество");
        TableColumn<StudentTableList, String> statusColumn = new TableColumn<>("Статус");
        TableColumn<StudentTableList, String> phoneColumn = new TableColumn<>("Телефон");
        TableColumn<StudentTableList, String> mailColumn = new TableColumn<>("Почта");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        grouppColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname")); 
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));               
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusSTR"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));

        AMTable.setItems(FXCollections.observableArrayList(new DatabaseHandler().getStudents())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, grouppColumn, surnameColumn, nameColumn, patronymicColumn, statusColumn, phoneColumn, mailColumn); //добавление колонок
    }
    
    private void initTableGroups()
    {
        AMTable.getColumns().clear(); //очистка таблицы
        TableColumn<Group, Integer> idColumn = new TableColumn<>("id");
        TableColumn<Group, Integer> courseColumn = new TableColumn<>("Курс");
        TableColumn<Group, String> grouppColumn = new TableColumn<>("Название");
        TableColumn<Group, Integer> yearColumn = new TableColumn<>("Год начала обучения");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        grouppColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        AMTable.setItems(FXCollections.observableArrayList(new DatabaseHandler().getGroups())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, courseColumn, grouppColumn, yearColumn); //добавление колонок
    }
    
    private void initTableExams()
    {
        AMTable.getColumns().clear(); //очистка таблицы
        TableColumn<ExamTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<ExamTableList, String> examColumn = new TableColumn<>("Экзамен");
        TableColumn<ExamTableList, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<ExamTableList, String> teacherColumn = new TableColumn<>("Преподаватель");
        TableColumn<ExamTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<ExamTableList, String> typeColumn = new TableColumn<>("Тип экзамена");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("name"));                
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher")); 
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); 
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("mark_st"));

        AMTable.setItems(FXCollections.observableArrayList(new DatabaseHandler().getExams())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, groupColumn, examColumn, teacherColumn, dateColumn, typeColumn); //добавление колонок
        
        idColumn.getTableView().getSortOrder().add(idColumn); 
        idColumn.setSortType(TableColumn.SortType.DESCENDING); //установка сортировки по умолчанию
    }
    
    private void initTableMarks()
    {
        AMTable.getColumns().clear(); //очистка таблицы
        TableColumn<MarkTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<MarkTableList, String> examColumn = new TableColumn<>("Экзамен");
        TableColumn<MarkTableList, String> studentColumn = new TableColumn<>("ФИО студента");
        TableColumn<MarkTableList, String> teacherColumn = new TableColumn<>("ФИО преподавателя");
        TableColumn<MarkTableList, Integer> markColumn = new TableColumn<>("Оценка");
        TableColumn<MarkTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<MarkTableList, Integer> retakeColumn = new TableColumn<>("№ пересдачи");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("exam")); 
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));               
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        retakeColumn.setCellValueFactory(new PropertyValueFactory<>("retake"));

        AMTable.setItems(FXCollections.observableArrayList(new DatabaseHandler().getMarks())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, examColumn, studentColumn, teacherColumn, markColumn, dateColumn, retakeColumn); //добавление колонок
    }
    
    private class CallbackImplRestorePassword implements Callback<TableColumn<UsersTableList, Void>, TableCell<UsersTableList, Void>> //работа с кнопкой "пересдача"
    {
        public CallbackImplRestorePassword() {}

        @Override
        public TableCell<UsersTableList, Void> call(final TableColumn<UsersTableList, Void> param)
        {
            final TableCell<UsersTableList, Void> cell = new TableCell<UsersTableList, Void>()
            {                
                @Override
                public void updateItem(Void item, boolean empty) {
                    Button btn = new Button("Восстановить пароль");                    
                    btn.setOnAction(new EventHandler<javafx.event.ActionEvent>()
                    {
                        @Override
                        public void handle(javafx.event.ActionEvent event)
                        {
                            Attenuation pushUp = new Attenuation(labelPushUp); //добавление эффекта затухания
                            UsersTableList data = getTableView().getItems().get(getIndex()); //получение строчки на которую было произведено нажатие
                            String login = Translit.cyr2lat(data.getSurname()+data.getName()); //генерация логина из имени и фамилии
                            String password = ((int) (Math.random() * 900000) + 99999) + ""; //генерация пароля
                            if(new DatabaseHandler().updateUserLoginAndPassword(data.getId(), login, password) == 1) //если восстановление логина и пароля удастся
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Восстановление уч. записи");                                
                                alert.setHeaderText(null);
                                alert.setContentText("Учётная запись восстановлена.\nДанные для входа:\nЛогин: " + 
                                        login +"\nПароль: " + password);
                                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
                                alert.showAndWait();
                            }
                            else
                            {
                                labelPushUp.setText("ОШИБКА: восстановить пароль не удалось");
                                labelPushUp.setTextFill(Color.web("#ff0000"));
                                pushUp.playAnim();
                            }
                            System.out.println(data.getId());
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
