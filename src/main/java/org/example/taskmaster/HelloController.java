package org.example.taskmaster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.LinkGitHub;
import project.Project;
import project.ProjectDAOImpl;
import projectuser.ProjectUserDAOImpl;
import user.User;
import user.UserDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * Контроллер для управления главным окном приложения.
 * Отображает информацию о пользователе и проектах, позволяет перейти к редактированию проекта.
 *
 * @author Дмитрий Куков
 * @date 28.05.2024
 */
public class HelloController implements Initializable {
    // Объявление компонентов интерфейса JavaFX
    @FXML
    private Button profileBTN, editBtn;
    @FXML
    private GridPane prGrid;
    @FXML
    private Label nPosts;
    @FXML
    private Label nameTitle, postTitle;
    private List<Project> projects;
    private ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();

    /**
     * Инициализация контроллера и загрузка данных при открытии окна.
     * Отображает информацию о пользователе и список проектов.
     *
     * @param url Ссылка на ресурс
     * @param resourceBundle Ресурсный пакет данных
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDAOImpl userDAO = new UserDAOImpl();
        int cur_id = userDAO.getCurrentUserId();
        User user = userDAO.getUserById(cur_id);

        nameTitle.setText(user.getName() + " " + user.getSurname());
        postTitle.setText(user.getPost());

        projects = new ArrayList<>();
        if (user.getPost().equals("Преподаватель")) {
            projects.addAll(getProjects());
        } else {
            int curUserId = user.getId();
            projects.addAll(projectUserDAO.getUserProjects(curUserId));
        }
        boolean isStudent = user.getPost().equals("Студент");
        nPosts.setText(String.valueOf(projects.size() + " Проектов"));

        int column = 0;
        int row = 1;
        try {
            for (Project project : projects) {
                if (isStudent && project.getStatus().equals("Архивный")) {
                    continue;
                }
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("project.fxml"));

                Pane pane = fxmlLoader.load();
                PrController prContr = fxmlLoader.getController();
                prContr.setData(project);

                // Добавление обработчика событий для клика на Pane
                pane.setOnMouseClicked(event -> handlePaneClick(project, pane));

                if (column == 3) {
                    column = 0;
                    ++row;
                }
                prGrid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(50));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение списка проектов.
     *
     * @return Список проектов
     */
    private List<Project> getProjects() {
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();
        List<Project> ls = new ArrayList<>();
        ls = projectDAO.getAllProjects();
        return ls;
    }
    /**
     * Обработка нажатия на панель проекта.
     * Открывает окно для редактирования выбранного проекта.
     *
     * @param project Выбранный проект
     * @param pane Панель проекта
     */
    private void handlePaneClick(Project project, Pane pane) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("projectForm.fxml"));
            Parent root = fxmlLoader.load();

            FormController formController = fxmlLoader.getController();
            formController.setProjectName(project.getName());
            formController.setSelectedProject(project);

            stage.setTitle("Project Form");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Обработка нажатия на кнопку редактирования профиля.
     * Закрывает текущее окно и открывает окно профиля.
     *
     * @throws IOException Ошибка ввода-вывода
     */
    public void onEditProfileBtnClick() throws IOException{
        Stage stage = (Stage) profileBTN.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("profile.fxml"));
        primaryStage.setTitle("Profile");
        primaryStage.setScene(new Scene(root, 1455, 733));
        primaryStage.show();
    }

    /**
     * Обработка нажатия на кнопку редактирования проекта.
     * В зависимости от должности пользователя, открывает окно редактирования проекта или выводит сообщение об ошибке.
     *
     * @throws IOException Ошибка ввода-вывода
     */
    public void onEditProjectBtnClick() throws IOException {
        UserDAOImpl userDAO = new UserDAOImpl();
        int cur_id = userDAO.getCurrentUserId();
        User user = userDAO.getUserById(cur_id);

        if (user.getPost().equals("Преподаватель")) {
            Stage stage = (Stage) editBtn.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("projectEdit.fxml"));
            primaryStage.setTitle("ProjectEdit");
            primaryStage.setScene(new Scene(root, 1455, 733));
            primaryStage.show();
        } else {
            nameTitle.setTextFill(Color.RED);
            postTitle.setTextFill(Color.RED);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                nameTitle.setTextFill(Color.WHITE);
                postTitle.setTextFill(Color.WHITE);
            }));
            timeline.play();
        }
    }
}