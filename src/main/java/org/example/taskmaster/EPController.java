package org.example.taskmaster;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import project.LinkGitHub;
import project.Project;
import project.ProjectDAOImpl;
import projectuser.ProjectUser;
import projectuser.ProjectUserDAOImpl;
import user.User;
import user.UserDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * Класс EPController отвечает за управление окном редактирования проектов,
 * где пользователь может добавлять/удалять участников, создавать/редактировать проекты и изменять статус проектов.
 * Данный контроллер также отображает список проектов с возможностью выбора для редактирования.
 *
 * @author Дмитрий Куков
 * @date 28.05.2024
 **/
public class EPController implements Initializable {
    @FXML
    private GridPane projectGrid;
    @FXML
    private Button backBtn, addUserBtn, deleteUserBtn, createProjectBtn, deleteProjectBtn, changeProjectBtn;
    @FXML
    private Pane corePane;
    @FXML
    private TextField nameField, termField;
    @FXML
    private TextField userField, surnameField, groupField;
    @FXML
    private ChoiceBox<String> statusBox;
    @FXML
    private Label error, title;

    private boolean isEditingMode = false;
    private Project selectedProject;
    private List<Project> projects;
    private List<User> addedUsers = new ArrayList<>();
    /**
     * Метод initialize инициализирует начальное состояние окна редактирования проектов.
     * Он заполняет таблицу проектов, добавляет статусы и устанавливает обработчики событий.
     */
        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusBox.getItems().addAll("Планируется");
        statusBox.setValue("Планируется");

        projects = new ArrayList<>(getProjects());

        int column = 1;
        int row = 0;
        try {
            for (Project project : projects) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("project_mini.fxml"));

                Pane pane = fxmlLoader.load();
                PrMiniController prContr= fxmlLoader.getController();
                prContr.setData(project);

                // Добавление обработчика событий для клика на Pane
                pane.setOnMouseClicked(event -> handlePaneClick(project, pane));
                System.out.println("Проект: " + project.getName());

                if(row == 1) {
                    row++;
                }
                projectGrid.add(pane, column, row++);
                GridPane.setMargin(pane, new Insets(0, 20, 0, 0));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private List<Project> getProjects() {
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();
        List<Project> ls = new ArrayList<>();
        ls = projectDAO.getAllProjects();
        return ls;
    }
    /**
     * Метод handlePaneClick обрабатывает событие клика на конкретный проект в списке проектов.
     * Он устанавливает выбранный проект для редактирования.
     */
    private void handlePaneClick(Project project, Pane pane) {
        isEditingMode = true;
        selectedProject = project;
        title.setText("Редактор: " + selectedProject.getName());

        addedUsers.clear();
        statusBox.getItems().clear();
        statusBox.getItems().addAll("Планируется","Назначан","В процессе","Завершен", "Архивный");
        statusBox.setValue(project.getStatus());
    }
    /**
     * Метод onAddUserBtnClick вызывается при нажатии кнопки "Добавить студента".
     * Он добавляет выбранного студента в список добавленных участников проекта.
     */
    public void onAddUserBtnClick() {
        String userName = userField.getText();
        String userSurname = surnameField.getText();
        String userGroup = groupField.getText();

        UserDAOImpl userDAO = new UserDAOImpl();
        User existingUser = userDAO.findUserByDetails(userName, userSurname, userGroup);

        if (existingUser != null) {
            addedUsers.add(existingUser);
            error.setText("Студент добавлен: " + existingUser.getName());
            error.setStyle("-fx-text-fill: green;");
        } else {
            error.setText("Студент не найден");
            error.setStyle("-fx-text-fill: red;");
        }
    }
    /**
     * Метод onDeleteUserBtnClick вызывается при нажатии кнопки "Удалить студента".
     * Он удаляет выбранного студента из списка добавленных участников проекта.
     */
    public void onDeleteUserBtnClick() {
        String userName = userField.getText();
        String userSurname = surnameField.getText();
        String userGroup = groupField.getText();


        UserDAOImpl userDAO = new UserDAOImpl();
        User existingUser = userDAO.findUserByDetails(userName, userSurname, userGroup);
        if (isEditingMode) {
            //удаление юзера из бд
            if (existingUser == null) {
                error.setText("Студент не найден");
                return;
            }
            if (existingUser.getName().equals(userName) && existingUser.getSurname().equals(userSurname) && existingUser.getGroupus().equals(userGroup)) {
                ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();
                if (projectUserDAO.checkUserInProject(existingUser.getId())) {
                    ProjectUser projectUser = new ProjectUser(selectedProject.getId(), existingUser.getId(), "Неизвестен");
                    projectUserDAO.deleteProjectUser(projectUser);
                    System.out.println("Студент убран из бд");
                    error.setText("Cтудент " + existingUser.getName() + " удален");
                }
            } else {
                System.out.println("Студент в бд не найден");
                error.setText("Студент не найден");
            }

            //удаление юзера из ОЗУ
            for (User user : addedUsers) {
                if (user.getName().equals(userName) && user.getSurname().equals(userSurname) && user.getGroupus().equals(userGroup)) {
                    addedUsers.remove(user);
                    System.out.println("Студент убран из ОЗУ");
                    error.setText("Cтудент " + user.getName() + " удален");
                } else {
                    System.out.println("Студент в ОЗУ не найден");
                    error.setText("Студент не найден");
                }
            }
        } else {
            // Логика для удаления человека
            for (User user : addedUsers) {
                if (user.getName().equals(userName) && user.getSurname().equals(userSurname) && user.getGroupus().equals(userGroup)) {
                    addedUsers.remove(user);
                    error.setText("Cтудент " + user.getName() + " убран");
                } else {
                    error.setText("Студент не найден");
                }
            }
        }
    }
    /**
     * Метод onCreateProjectBtnClick вызывается при нажатии кнопки "Создать проект".
     * Он создает новый проект с указанными параметрами и добавленными участниками.
     */
    public void onCreateProjectBtnClick() {
        ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();
        Project newProject = new Project();
        if (isEditingMode) {
            error.setText("Проект уже создан");
        } else {
            // Логика для создания нового проекта
            String projectName = nameField.getText();
            String projectTerm = termField.getText();
            String projectStatus = statusBox.getValue();
            if (projectName.isEmpty()) {
                error.setText("Название не может быть пустым. Введите название");
                return;
            }
            if (!projectTerm.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate termDate = LocalDate.parse(projectTerm, formatter);
                    newProject.setTerm(termDate.toString()); // Assuming you need to store the date as String
                } catch (DateTimeParseException e) {
                    error.setText("Ошибка введеного формата(YYYY-MM-DD.)");
                    return;
                }
            } else {
                error.setText("Срок не может быть пустым. Введите срок");
                return;
            }
            if (!addedUsers.isEmpty()) {
                newProject.setStatus("Назначан");
            }

            newProject.setName(nameField.getText());
            newProject.setTerm(termField.getText());
            newProject.setStatus(statusBox.getValue());
            newProject.setCountUs(addedUsers.size());

            if (!addedUsers.isEmpty()) {
                newProject.setStatus("Назначан");
            }

            projectDAO.addProject(newProject);
            newProject.setId(projectDAO.getProjectIdByName(newProject.getName()));

            for (User user : addedUsers) {
                ProjectUser projectUser = new ProjectUser(newProject.getId(), user.getId(), "Неизвестен");
                projectUserDAO.addProjectUser(projectUser);
            }
            error.setText(newProject.getName() + " создан" + "("+newProject.getCountUs() + ")");

            addedUsers.clear();
        }
    }
    /**
     * Метод onDeleteProjectBtnClick вызывается при нажатии кнопки "Удалить проект".
     * Он удаляет выбранный проект из базы данных и все связанные с ним записи.
     */
    public void onDeleteProjectBtnClick() {
        ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();

        if (isEditingMode && selectedProject != null) {
            // Delete the associated GitHub link for the project
            LinkGitHub linkGitHub = projectDAO.getLinkGitHubByProjectId(selectedProject.getId());
            if (linkGitHub != null) {
                projectDAO.deleteLinkGitHub(linkGitHub);
                System.out.println("GitHub link for the project " + selectedProject.getName() + " deleted.");
            }

            // Remove the project from the database
            projectUserDAO.deleteProjectUserByProjectId(selectedProject.getId());
            projectDAO.deleteProject(selectedProject.getId());

            error.setText(selectedProject.getName() + " deleted");

            selectedProject = null;
            isEditingMode = false;
        } else {
            error.setText("Выберите проект для удаления");
        }
    }
    /**
     * Метод onChangeProjectBtnClick вызывается при нажатии кнопки "Изменить проект".
     * Он обновляет данные выбранного проекта с новыми параметрами и участниками.
     */
    public void onChangeProjectBtnClick() {
        ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();
        if (isEditingMode) {
            // Update the selected project details
            // Логика для создания нового проекта
            String projectName = nameField.getText();
            String projectTerm = termField.getText();
            String projectStatus = statusBox.getValue();

            if (!projectTerm.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate termDate = LocalDate.parse(projectTerm, formatter);
                } catch (DateTimeParseException e) {
                    error.setText("Ошибка введеного формата(YYYY-MM-DD.)");
                    return;
                }
            }


            selectedProject.setName(selectedProject.getName());
            selectedProject.setTerm(selectedProject.getTerm());
            selectedProject.setStatus(selectedProject.getStatus());

            if (!projectName.isEmpty()) {selectedProject.setName(projectName);}
            if (!projectTerm.isEmpty()) {selectedProject.setTerm(projectTerm);}
            if (!projectStatus.isEmpty()) {selectedProject.setStatus(projectStatus);}
            if (!addedUsers.isEmpty()) {
                selectedProject.setStatus("Назначан");
            }

            int size = projectUserDAO.countUsersInProject(selectedProject.getId());
            selectedProject.setCountUs(addedUsers.size() + size);
            projectDAO.updateProject(selectedProject);
            selectedProject.setId(projectDAO.getProjectIdByName(selectedProject.getName()));

            for (User user : addedUsers) {
                ProjectUser projectUser = new ProjectUser(selectedProject.getId(), user.getId(), "Неизвестен");
                projectUserDAO.addProjectUser(projectUser);
            }

            error.setText(selectedProject.getName() + " обновлен" + "(" + selectedProject.getCountUs() + ")");
            addedUsers.clear();
        } else {
            // Логика для создания нового проекта
            error.setText("Новый проект нельзя изменить");
        }
    }
    /**
     * Метод onReturnBtnClick вызывается при нажатии кнопки "Назад".
     * Он закрывает текущее окно редактирования проектов и возвращает пользователя к предыдущему экрану.
     */
    public void onReturnBtnClick() throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("hello-view.fxml"));
        primaryStage.setTitle("TaskMaster");
        primaryStage.setScene(new Scene(root, 1455, 733));
        primaryStage.show();
    }
}
