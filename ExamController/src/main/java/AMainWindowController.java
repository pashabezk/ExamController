import TableLists.ExamTableList;
import TableLists.MarkTableList;
import TableLists.StudentTableList;
import TableLists.UsersTableList;
import animations.Attenuation;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class AMainWindowController implements Initializable
{
    @FXML private ListView<String> AMListView;
    @FXML private TableView AMTable;
    @FXML private Label labelPushUp;
    @FXML private SplitPane fxSplitPane;

    private Attenuation pushUp; //объект анимации пуш-ап уведомления

    private int selectedItemId = 0; //идентификатор объекта, на который было совершено нажатие
    MarkTableList selectedMark; //оценка, которая была выбрана
    private String selectedTableName = "Пользователи";

    private class CodeName
    {
        private int code;
        private String name;

        public CodeName(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {return code;}
        public String getName() {return name;}

        @Override
        public String toString() {return this.name;}
    }

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
    private void handleButtonAdd(ActionEvent event) //кнопка "добавить"
    {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        switch(selectedTableName) //выбор открытия окна в зависимости от выбранного элемента списка
        {
            case "Группы":
                loader.setLocation(getClass().getResource("/AddGroup.fxml"));
                stage.setTitle(GLOBAL.TITLE + " - создание группы");
                stage.setMinWidth(290);
                stage.setMinHeight(250);
                break;

            case "Студенты":
                loader.setLocation(getClass().getResource("/AddStudent.fxml"));
                stage.setTitle(GLOBAL.TITLE + " - создание студента");
                stage.setMinWidth(630);
                stage.setMinHeight(330);
                break;

            case "Экзамены":
                loader.setLocation(getClass().getResource("/AddExam.fxml"));
                stage.setTitle(GLOBAL.TITLE + " - создание экзамена");
                stage.setMinWidth(630);
                stage.setMinHeight(300);
                break;

            case "Оценки":
                loader.setLocation(getClass().getResource("/AddMark.fxml"));
                stage.setTitle(GLOBAL.TITLE + " - добавление оценки");
                stage.setMinWidth(630);
                stage.setMinHeight(270);
                break;

            default: //пользователи
                loader.setLocation(getClass().getResource("/AddUser.fxml"));
                stage.setTitle(GLOBAL.TITLE + " - создание пользователя");
                stage.setMinWidth(560);
                stage.setMinHeight(400);
                break;
        }
        try{ loader.load();
        } catch (IOException ex) {ex.printStackTrace();}
        Parent root = loader.getRoot();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(ExamController.class.getResourceAsStream(GLOBAL.ICONURL)));
        stage.setOnHidden(new EventHandler<WindowEvent>() { //действия при закрытии окна
            @Override
            public void handle(WindowEvent event) {
                switch(selectedTableName) //обновление таблицы в зависимости от выбранного элемента списка
                {
                    case "Пользователи": initTableUsers(); break;
                    case "Группы": initTableGroups(); break;
                    case "Студенты": initTableStudents(); break;
                    case "Экзамены": initTableExams(); break;
                    case "Оценки": initTableMarks(); break;
                }
            }
        });
        stage.show();
    }

    @FXML
    private void handleButtonDelete(ActionEvent event) //кнопка "удалить"
    {
        labelPushUp.setTextFill(GLOBAL.RED);

        switch(selectedTableName) //удаление в зависимости от выбранного элемента списка
        {
            case "Пользователи":
                switch(DatabaseHandler.isUserCanBeDeleted(selectedItemId))
                {
                    case 1: //в случае, если пользователя не привязан к экзаменам или оценкам
                        if (DatabaseHandler.deleteUser(selectedItemId) == 1)
                        {
                            labelPushUp.setText("Запись удалёна");
                            labelPushUp.setTextFill(GLOBAL.BLUE);
                            initTableUsers(); //перерисовка таблицы
                        }
                        else labelPushUp.setText("ОШИБКА: удалить запись не удалось");
                        break;
                    case -1: labelPushUp.setText("ОШИБКА: не удалось подключиться к БД"); break;
                    default: labelPushUp.setText("ОШИБКА: к пользователю привязаны экзамены"); break;
                }
                break;

            case "Группы":
                switch(DatabaseHandler.isGroupCanBeDeleted(selectedItemId))
                {
                    case 1: //в случае, если к группе не привязаны студенты или экзамены
                        if (DatabaseHandler.deleteGroup(selectedItemId) == 1)
                        {
                            labelPushUp.setText("Запись удалёна");
                            labelPushUp.setTextFill(GLOBAL.BLUE);
                            initTableGroups(); //перерисовка таблицы
                        }
                        else labelPushUp.setText("ОШИБКА: удалить запись не удалось");
                        break;
                    case -1: labelPushUp.setText("ОШИБКА: не удалось подключиться к БД"); break;
                    case 2: labelPushUp.setText("ОШИБКА: к группе привязаны студенты"); break;
                    case 3: labelPushUp.setText("ОШИБКА: к группе привязаны экзамены"); break;
                }
                break;

            case "Студенты":
                switch(DatabaseHandler.isStudentCanBeDeleted(selectedItemId))
                {
                    case 1: //в случае, если к студенту не привязаны оценки
                        if (DatabaseHandler.deleteStudent(selectedItemId) == 1)
                        {
                            labelPushUp.setText("Запись удалёна");
                            labelPushUp.setTextFill(GLOBAL.BLUE);
                            initTableStudents(); //перерисовка таблицы
                        }
                        else labelPushUp.setText("ОШИБКА: удалить запись не удалось");
                        break;
                    case -1: labelPushUp.setText("ОШИБКА: не удалось подключиться к БД"); break;
                    case 0: labelPushUp.setText("ОШИБКА: к студенту привязаны оценки"); break;
                }
                break;

            case "Экзамены":
                switch(DatabaseHandler.isExamCanBeDeleted(selectedItemId))
                {
                    case 1: //в случае, если к экзамену не привязаны оценки
                        if (DatabaseHandler.deleteExam(selectedItemId) == 1)
                        {
                            labelPushUp.setText("Запись удалёна");
                            labelPushUp.setTextFill(GLOBAL.BLUE);
                            initTableExams(); //перерисовка таблицы
                        }
                        else labelPushUp.setText("ОШИБКА: удалить запись не удалось");
                        break;
                    case -1: labelPushUp.setText("ОШИБКА: не удалось подключиться к БД"); break;
                    case 0: labelPushUp.setText("ОШИБКА: к экзамену привязаны оценки"); break;
                }
                break;

            case "Оценки":
                int retake = DatabaseHandler.getNumberOfRetakes(selectedMark.getStudentID(), selectedMark.getExamID());
                if(retake == -1) labelPushUp.setText("ОШИБКА: не удалось подключиться к БД");
                else if(retake==1) labelPushUp.setText("ОШИБКА: нельзя удалить первую сдачу");
                else
                {
                    if (DatabaseHandler.deleteMark(selectedItemId) == 1)
                    {
                        labelPushUp.setText("Запись удалёна");
                        labelPushUp.setTextFill(GLOBAL.BLUE);
                        initTableMarks(); //перерисовка таблицы
                    }
                    else labelPushUp.setText("ОШИБКА: удалить запись не удалось");
                }
                break;
        }
        pushUp.playAnim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pushUp = new Attenuation(labelPushUp); //применение анимации пуш-ап уведомления к labelPushUp

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
                selectedTableName = newValue;
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

        AMTable.setEditable(true); //разрешение редактирования таблицу
        AMTable.setOnMousePressed(new EventHandler<MouseEvent>() //установка прослушивателя на нажатие строчки в таблице
        {
            @Override
            public void handle(MouseEvent event) {
                switch(selectedTableName) //получение идентификатора нажатого объекта
                {
                    case "Пользователи": selectedItemId = ((UsersTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Студенты": selectedItemId = ((StudentTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Группы": selectedItemId = ((Group) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Экзамены": selectedItemId = ((ExamTableList) AMTable.getSelectionModel().getSelectedItem()).getId(); break;
                    case "Оценки":
                        selectedItemId = ((MarkTableList) AMTable.getSelectionModel().getSelectedItem()).getId();
                        selectedMark = (MarkTableList) AMTable.getSelectionModel().getSelectedItem();
                        break;
                }
            }
        });
    }

    public void initTableUsers()
    {
        AMTable.getColumns().clear(); //очистка таблицы

        //создание колонок таблицы
        TableColumn<UsersTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<UsersTableList, String> surnameColumn = new TableColumn<>("Фамилия");
        TableColumn<UsersTableList, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<UsersTableList, String> patronymicColumn = new TableColumn<>("Отчество");
        TableColumn<UsersTableList, CodeName> typeColumn = new TableColumn<>("Тип пользователя");
        TableColumn<UsersTableList, String> phoneColumn = new TableColumn<>("Телефон");
        TableColumn<UsersTableList, String> mailColumn = new TableColumn<>("Почта");
        TableColumn<UsersTableList, Void> btnColumn = new TableColumn("Пароль");

        //привязка фабрик для автоматического заполнения таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeSTR"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));

        //добавление опции редактирования ячеек
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования фамилии
        surnameColumn.setOnEditCommit(event -> //применение обработчика
        {
            UsersTableList user = event.getRowValue();
            user.setSurname(event.getNewValue());
            updateNotification(DatabaseHandler.updateUser(user));
        });

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования имени
        nameColumn.setOnEditCommit(event -> //применение обработчика
        {
            UsersTableList user = event.getRowValue();
            user.setName(event.getNewValue());
            updateNotification(DatabaseHandler.updateUser(user));
        });

        patronymicColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования отчества
        patronymicColumn.setOnEditCommit(event -> //применение обработчика
        {
            UsersTableList user = event.getRowValue();
            user.setPatronymic(event.getNewValue());
            updateNotification(DatabaseHandler.updateUser(user));
        });

        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования телефона
        phoneColumn.setOnEditCommit(event -> //применение обработчика
        {
            UsersTableList user = event.getRowValue();
            user.setPhone(event.getNewValue());
            updateNotification(DatabaseHandler.updateUser(user));
        });

        mailColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования почты
        mailColumn.setOnEditCommit(event -> //применение обработчика
        {
            UsersTableList user = event.getRowValue();
            user.setMail(event.getNewValue());
            updateNotification(DatabaseHandler.updateUser(user));
        });

        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(new CodeName(1, UsersTableList.TYPE_ADMINISTRATION_STAFF), new CodeName(2, UsersTableList.TYPE_TEACHER), new CodeName(3, UsersTableList.TYPE_DATABASE_ADMINISTRATION)))); //добавление возможности редактирования типа пользователя
        typeColumn.setOnEditCommit(event -> //применение обработчика
        {
            UsersTableList user = event.getRowValue();
            user.setType(event.getNewValue().getCode());
            updateNotification(DatabaseHandler.updateUser(user));
        });

        //добавление кнопок "восстановить пароль"
        Callback<TableColumn<UsersTableList, Void>, TableCell<UsersTableList, Void>> cellFactoryRestorePassword = new CallbackImplRestorePassword();
        btnColumn.setCellFactory(cellFactoryRestorePassword);

        AMTable.setItems(FXCollections.observableArrayList(DatabaseHandler.getUsers())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, surnameColumn, nameColumn, patronymicColumn, typeColumn, phoneColumn, mailColumn, btnColumn); //добавление колонок
    }

    private void initTableStudents()
    {
        AMTable.getColumns().clear(); //очистка таблицы

        //создание колонок таблицы
        TableColumn<UsersTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<StudentTableList, Group> groupColumn = new TableColumn<>("Группа");
        TableColumn<StudentTableList, String> surnameColumn = new TableColumn<>("Фамилия");
        TableColumn<StudentTableList, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<StudentTableList, String> patronymicColumn = new TableColumn<>("Отчество");
        TableColumn<StudentTableList, CodeName> statusColumn = new TableColumn<>("Статус");
        TableColumn<StudentTableList, String> phoneColumn = new TableColumn<>("Телефон");
        TableColumn<StudentTableList, String> mailColumn = new TableColumn<>("Почта");

        //привязка фабрик для автоматического заполнения таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusSTR"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));

        //добавление опции редактирования ячеек
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования фамилии
        surnameColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setSurname(event.getNewValue());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования имени
        nameColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setName(event.getNewValue());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        patronymicColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования отчества
        patronymicColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setPatronymic(event.getNewValue());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования телефона
        phoneColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setPhone(event.getNewValue());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        mailColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования почты
        mailColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setMail(event.getNewValue());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(new CodeName(1, StudentTableList.STATUS_EDUCATION), new CodeName(2, StudentTableList.STATUS_EXPEL), new CodeName(3, StudentTableList.STATUS_ACADEMIC_LEAVE), new CodeName(4, StudentTableList.STATUS_FINISH_EDUCATION)))); //добавление возможности редактирования статуса обучения студента
        statusColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setStatus(event.getNewValue().getCode());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        groupColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DatabaseHandler.getGroups()))); //добавление возможности редактирования группы
        groupColumn.setOnEditCommit(event -> //применение обработчика
        {
            StudentTableList student = event.getRowValue();
            student.setGroup(event.getNewValue().getId(), event.getNewValue().getName());
            updateNotification(DatabaseHandler.updateStudent(student));
        });

        AMTable.setItems(FXCollections.observableArrayList(DatabaseHandler.getStudents())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, groupColumn, surnameColumn, nameColumn, patronymicColumn, statusColumn, phoneColumn, mailColumn); //добавление колонок
    }

    private void initTableGroups()
    {
        AMTable.getColumns().clear(); //очистка таблицы

        //создание колонок таблицы
        TableColumn<Group, Integer> idColumn = new TableColumn<>("id");
        TableColumn<Group, Integer> courseColumn = new TableColumn<>("Курс");
        TableColumn<Group, String> grouppColumn = new TableColumn<>("Название");
        TableColumn<Group, Integer> yearColumn = new TableColumn<>("Год начала обучения");

        //привязка фабрик для автоматического заполнения таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        grouppColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        //добавление опции редактирования ячеек
        grouppColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования названия группы
        grouppColumn.setOnEditCommit(event -> //применение обработчика
        {
            Group group = event.getRowValue();
            group.setName(event.getNewValue());
            updateNotification(DatabaseHandler.updateGroup(group));
        });

        courseColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(1, 2, 3, 4))); //добавление возможности редактирования курса
        courseColumn.setOnEditCommit(event -> //применение обработчика
        {
            Group group = event.getRowValue();
            group.setCourse(event.getNewValue());
            updateNotification(DatabaseHandler.updateGroup(group));
        });

        ObservableList list = FXCollections.observableArrayList();
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i>1999; i--) //получение текущего года и создание списка
            list.add(i); //заполнение списка "год начала обучения"
        yearColumn.setCellFactory(ComboBoxTableCell.forTableColumn(list)); //добавление возможности редактирования года начала обучения
        yearColumn.setOnEditCommit(event -> //применение обработчика
        {
            Group group = event.getRowValue();
            group.setYear(event.getNewValue());
            updateNotification(DatabaseHandler.updateGroup(group));
        });

        AMTable.setItems(FXCollections.observableArrayList(DatabaseHandler.getGroups())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, courseColumn, grouppColumn, yearColumn); //добавление колонок
    }

    private void initTableExams()
    {
        AMTable.getColumns().clear(); //очистка таблицы

        //создание колонок таблицы
        TableColumn<ExamTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<ExamTableList, String> examColumn = new TableColumn<>("Экзамен");
        TableColumn<ExamTableList, Group> groupColumn = new TableColumn<>("Группа");
        TableColumn<ExamTableList, UsersTableList> teacherColumn = new TableColumn<>("Преподаватель");
        TableColumn<ExamTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<ExamTableList, CodeName> typeColumn = new TableColumn<>("Тип экзамена");

        //привязка фабрик для автоматического заполнения таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("markStSTR"));

        //добавление опции редактирования ячеек
        groupColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DatabaseHandler.getGroups()))); //добавление возможности редактирования группы
        groupColumn.setOnEditCommit(event -> //применение обработчика
        {
            ExamTableList exam = event.getRowValue();
            exam.setGroup(event.getNewValue().getId(), event.getNewValue().getName());
            updateNotification(DatabaseHandler.updateExam(exam));
        });

        examColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования названия экзамена
        examColumn.setOnEditCommit(event -> //применение обработчика
        {
            ExamTableList exam = event.getRowValue();
            exam.setName(event.getNewValue());
            updateNotification(DatabaseHandler.updateExam(exam));
        });

        teacherColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DatabaseHandler.getUsers()))); //добавление возможности редактирования преподавателя
        teacherColumn.setOnEditCommit(event -> //применение обработчика
        {
            ExamTableList exam = event.getRowValue();
            exam.setTeacher(event.getNewValue().getId(), event.getNewValue().toString());
            updateNotification(DatabaseHandler.updateExam(exam));
        });

        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования даты
        dateColumn.setOnEditCommit(event -> //применение обработчика
        {
            ExamTableList exam = event.getRowValue();
            exam.setDate(event.getNewValue());
            updateNotification(DatabaseHandler.updateExam(exam));
        });

        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(new CodeName(1, ExamTableList.MARK_ST_DIFF), new CodeName(2, ExamTableList.MARK_ST_NOT_DIFF)))); //добавление возможности редактирования типа экзамена (диф/не диф)
        typeColumn.setOnEditCommit(event -> //применение обработчика
        {
            ExamTableList exam = event.getRowValue();
            exam.setMarkSt(event.getNewValue().getCode());
            updateNotification(DatabaseHandler.updateExam(exam));
        });

        AMTable.setItems(FXCollections.observableArrayList(DatabaseHandler.getExams())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, groupColumn, examColumn, teacherColumn, dateColumn, typeColumn); //добавление колонок

        idColumn.getTableView().getSortOrder().add(idColumn);
        idColumn.setSortType(TableColumn.SortType.DESCENDING); //установка сортировки по умолчанию
    }

    private void initTableMarks()
    {
        AMTable.getColumns().clear(); //очистка таблицы

        //создание колонок таблицы
        TableColumn<MarkTableList, Integer> idColumn = new TableColumn<>("id");
        TableColumn<MarkTableList, ExamTableList> examColumn = new TableColumn<>("Экзамен");
        TableColumn<MarkTableList, StudentTableList> studentColumn = new TableColumn<>("ФИО студента");
        TableColumn<MarkTableList, UsersTableList> teacherColumn = new TableColumn<>("ФИО преподавателя");
        TableColumn<MarkTableList, Integer> markColumn = new TableColumn<>("Оценка");
        TableColumn<MarkTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<MarkTableList, Integer> retakeColumn = new TableColumn<>("№ пересдачи");

        //привязка фабрик для автоматического заполнения таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("exam"));
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        retakeColumn.setCellValueFactory(new PropertyValueFactory<>("retake"));

        //добавление опции редактирования ячеек
        examColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DatabaseHandler.getExams()))); //добавление возможности редактирования группы
        examColumn.setOnEditCommit(event -> //применение обработчика
        {
            MarkTableList mark = event.getRowValue();
            mark.setExam(event.getNewValue().getId(), event.getNewValue().getName());
            updateNotification(DatabaseHandler.updateMark(mark));
        });

        studentColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DatabaseHandler.getStudents()))); //добавление возможности редактирования студента
        studentColumn.setOnEditCommit(event -> //применение обработчика
        {
            MarkTableList mark = event.getRowValue();
            mark.setStudent(event.getNewValue().getId(), event.getNewValue().toString());
            updateNotification(DatabaseHandler.updateMark(mark));
        });

        teacherColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DatabaseHandler.getUsers()))); //добавление возможности редактирования преподавателя
        teacherColumn.setOnEditCommit(event -> //применение обработчика
        {
            MarkTableList mark = event.getRowValue();
            mark.setUser(event.getNewValue().getId(), event.getNewValue().toString());
            updateNotification(DatabaseHandler.updateMark(mark));
        });

        markColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5))); //добавление возможности редактирования оценки
        markColumn.setOnEditCommit(event -> //применение обработчика
        {
            MarkTableList mark = event.getRowValue();
            if (((mark.getMark() <= 1) && (event.getNewValue() <= 1)) || ((mark.getMark() >= 2) && (event.getNewValue() >= 2)))
            {
                mark.setMark(event.getNewValue());
                updateNotification(DatabaseHandler.updateMark(mark));
            }
            else
            {
                labelPushUp.setTextFill(GLOBAL.RED);
                labelPushUp.setText("ОШИБКА: тип экзамена и оценки должен совпадать");
                pushUp.playAnim();
            }
        });

        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn()); //добавление возможности редактирования даты
        dateColumn.setOnEditCommit(event -> //применение обработчика
        {
            MarkTableList mark = event.getRowValue();
            mark.setDate(event.getNewValue());
            updateNotification(DatabaseHandler.updateMark(mark));
        });

        retakeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(1, 2, 3))); //добавление возможности редактирования номера пересдачи
        retakeColumn.setOnEditCommit(event -> //применение обработчика
        {
            MarkTableList mark = event.getRowValue();
            mark.setRetake(event.getNewValue());
            updateNotification(DatabaseHandler.updateMark(mark));
        });

        AMTable.setItems(FXCollections.observableArrayList(DatabaseHandler.getMarks())); //добавление информации в таблицу
        AMTable.getColumns().addAll(idColumn, examColumn, studentColumn, teacherColumn, markColumn, dateColumn, retakeColumn); //добавление колонок

        idColumn.getTableView().getSortOrder().add(idColumn);
        idColumn.setSortType(TableColumn.SortType.DESCENDING); //установка сортировки по умолчанию
    }

    private void updateNotification (int success) //уведомление об статусе редактирования
    {
        if (success==1) //внесение редактированной записи в базу данных
        {
            labelPushUp.setTextFill(GLOBAL.BLUE);
            labelPushUp.setText(GLOBAL.SUCCESS_UPDATE);
        }
        else //при неудавшемся обновлении данных
        {
            labelPushUp.setTextFill(GLOBAL.RED);
            labelPushUp.setText(GLOBAL.ERROR_DB_CONNECTION);
        }
        pushUp.playAnim();
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
                            UsersTableList data = getTableView().getItems().get(getIndex()); //получение строчки на которую было произведено нажатие
                            String login = Translit.cyr2lat(data.getSurname()+data.getName()); //генерация логина из имени и фамилии
                            String password = ((int) (Math.random() * 900000) + 99999) + ""; //генерация пароля
                            if(DatabaseHandler.updateUserLoginAndPassword(data.getId(), login, password) == 1) //если восстановление логина и пароля удастся
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
                                labelPushUp.setTextFill(GLOBAL.RED);
                                pushUp.playAnim(); //добавление эффекта затухания
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