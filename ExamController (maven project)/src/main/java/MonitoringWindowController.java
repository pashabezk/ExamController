import TableLists.ExamTableList;
import TableLists.RatingStudentTableList;
import TableLists.StudentTableList;
import TableLists.UsersTableList;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MonitoringWindowController implements Initializable
{
    @FXML
    private ListView<String> ASListView;
    @FXML
    private TableView tblStatement;
    
    public ArrayList<ExamTableList> exams; //список экзаменов преподавателя
        
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ObservableList<String> list = FXCollections.observableArrayList(); //список, из которого будет происходить вывод на экран
        list.add("Список экзаменов");
        list.add("Список студентов");
        list.add("Список преподавателей");
        list.add("Рейтинг групп");
        list.add("Рейтинг студентов");
        list.add("Список 'к отчислению'");
        list.add("Назначение стипендии");
        ASListView.setItems(list);
        
        ASListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() //добавление прослушивателя на список экзаменов, чтобы обновлять таблицу оценок при выборе экзамена
        {             
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) //установка прослушателя на список в левой колонке
            {
                tblStatement.getColumns().clear(); //очистка таблицы
                switch(newValue) //инициализация столбцов таблицы в зависимости от выбранного пункта
                {
                    case "Список экзаменов": initTableAllExam(); break;
                    case "Список студентов": initTableAllStudents(); break;
                    case "Список преподавателей": initTableAllUsers(); break;
                    case "Рейтинг групп": initTableRatingGroups(); break;
                    case "Рейтинг студентов": initTableRatingStudents(); break;
                    case "Список 'к отчислению'": initTableExpellList(); break;
                    case "Назначение стипендии": initTableStipendList(); break;
                }
            }
        });
    }
    
    private void initTableAllExam()
    {
        TableColumn<ExamTableList, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<ExamTableList, String> examColumn = new TableColumn<>("Экзамен");
        TableColumn<ExamTableList, String> teacherColumn = new TableColumn<>("Преподаватель");
        TableColumn<ExamTableList, String> dateColumn = new TableColumn<>("Дата");
        TableColumn<ExamTableList, String> typeColumn = new TableColumn<>("Тип экзамена");

        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("name"));                
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher")); 
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); 
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("mark_st"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getExams())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(groupColumn, examColumn, teacherColumn, dateColumn, typeColumn); //добавление колонок

        dateColumn.getTableView().getSortOrder().add(dateColumn); 
        dateColumn.setSortType(TableColumn.SortType.DESCENDING); //установка сортировки по умолчанию
    }
    
    private void initTableAllStudents()
    {
        TableColumn<StudentTableList, String> grouppColumn = new TableColumn<>("Группа");
        TableColumn<StudentTableList, String> surnameColumn = new TableColumn<>("Фамилия");
        TableColumn<StudentTableList, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<StudentTableList, String> patronymicColumn = new TableColumn<>("Отчество");
        TableColumn<StudentTableList, String> statusColumn = new TableColumn<>("Статус");
        TableColumn<StudentTableList, String> phoneColumn = new TableColumn<>("Телефон");
        TableColumn<StudentTableList, String> mailColumn = new TableColumn<>("Почта");

        grouppColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname")); 
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));               
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusSTR"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getStudents())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(grouppColumn, surnameColumn, nameColumn, patronymicColumn, statusColumn, phoneColumn, mailColumn); //добавление колонок

        grouppColumn.getTableView().getSortOrder().add(grouppColumn); 
        grouppColumn.setSortType(TableColumn.SortType.ASCENDING); //установка сортировки по умолчанию
    }
    
    private void initTableAllUsers()
    {
        TableColumn<UsersTableList, String> surnameColumn = new TableColumn<>("Фамилия");
        TableColumn<UsersTableList, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<UsersTableList, String> patronymicColumn = new TableColumn<>("Отчество");
        TableColumn<UsersTableList, String> typeColumn = new TableColumn<>("Тип пользователя");
        TableColumn<UsersTableList, String> phoneColumn = new TableColumn<>("Телефон");
        TableColumn<UsersTableList, String> mailColumn = new TableColumn<>("Почта");

        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname")); 
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));               
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeSTR"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getUsers())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(surnameColumn, nameColumn, patronymicColumn, typeColumn, phoneColumn, mailColumn); //добавление колонок

        surnameColumn.getTableView().getSortOrder().add(surnameColumn); 
        surnameColumn.setSortType(TableColumn.SortType.ASCENDING); //установка сортировки по умолчанию
    }
    
    private void initTableRatingStudents()
    {
        TableColumn<RatingStudentTableList, Integer> courseColumn = new TableColumn<>("Курс");
        TableColumn<RatingStudentTableList, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<RatingStudentTableList, String> studentColumn = new TableColumn<>("ФИО студента");
        TableColumn<RatingStudentTableList, Double> markColumn = new TableColumn<>("Ср. балл");

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course")); 
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));               
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getRatingStudents())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(courseColumn, groupColumn, studentColumn, markColumn); //добавление колонок
    }
    
    private void initTableRatingGroups()
    {
        TableColumn<RatingStudentTableList, Integer> courseColumn = new TableColumn<>("Курс");
        TableColumn<RatingStudentTableList, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<RatingStudentTableList, Double> markColumn = new TableColumn<>("Ср. балл");

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course")); 
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getRatingGroups())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(courseColumn, groupColumn, markColumn); //добавление колонок
    }
    
    private void initTableExpellList()
    {
        TableColumn<RatingStudentTableList, String> studentColumn = new TableColumn<>("ФИО студента");
        TableColumn<RatingStudentTableList, Integer> debtColumn = new TableColumn<>("Кол-во задолженностей");

        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student")); 
        debtColumn.setCellValueFactory(new PropertyValueFactory<>("debt"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getExpellList())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(studentColumn, debtColumn); //добавление колонок
    }
    
    private void initTableStipendList()
    {
        TableColumn<RatingStudentTableList, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<RatingStudentTableList, String> studentColumn = new TableColumn<>("ФИО студента");
        TableColumn<RatingStudentTableList, Integer> stipendColumn = new TableColumn<>("Размер стипендии");

        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student")); 
        stipendColumn.setCellValueFactory(new PropertyValueFactory<>("stipend"));

        tblStatement.setItems(FXCollections.observableArrayList(new DatabaseHandler().getStipendList())); //добавление информации в таблицу
        tblStatement.getColumns().addAll(groupColumn, studentColumn, stipendColumn); //добавление колонок
    }
}
