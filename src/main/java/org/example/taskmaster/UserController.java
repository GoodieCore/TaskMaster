package org.example.taskmaster;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import user.User;
import user.UserDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * Контроллер для управления пользователями.
 * Отображает и обрабатывает информацию о пользователях.
 *
 * @author Дмитрий Куков
 * @date 28.05.2024
 */
public class UserController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField groupus;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private ChoiceBox<String> choisePost;
    @FXML
    private Label error;
    @FXML
    private Label nameTitle;
    @FXML
    private Button backBtn;
    /**
     * Инициализация контроллера.
     * Устанавливает начальные значения и отображает информацию о текущем пользователе.
     * @param url URL ресурса
     * @param resourceBundle ресурс
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDAOImpl userDAO = new UserDAOImpl();
        int cur_id = userDAO.getCurrentUserId();
        User user = userDAO.getUserById(cur_id);
        nameTitle.setText(user.getName() + " " + user.getSurname());

        choisePost.getItems().addAll("Студент", "Преподаватель");
        choisePost.setValue("Студент");
    }
    /**
     * Обработчик нажатия кнопки "Вернуться".
     * Закрывает текущее окно и открывает главное окно.
     * @throws IOException
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
    /**
     * Обработчик нажатия кнопки "Сохранить".
     * Проверяет введенные данные и добавляет нового пользователя в базу данных.
     */
    public void onSaveBtnClick() {
        UserDAOImpl userDAO = new UserDAOImpl();

        if(checkingInput()) {
            User user = new User(0, name.getText(), surname.getText(), groupus.getText(), phone.getText(), email.getText(), choisePost.getValue());
            userDAO.addUser(user);

            int newUserId = userDAO.getLastUserId();
            userDAO.updateCurrentUserId(newUserId);

            error.setText("Успешно добавлено");
            error.setStyle("-fx-text-fill: green;");
        }
    }

    /**
     * Проверяет корректность введенных данных.
     * @return true, если данные введены корректно, иначе false.
     */
    public boolean checkingInput() {
        if(name.getText().isEmpty() || surname.getText().isEmpty() || groupus.getText().isEmpty()) {
            error.setText("Ошибка ввода данных");
            return false;
        }
        error.setText("");
        return true;
    }
    /**
     * Обновляет текущего пользователя в сессии.
     */
    public void onChangeSessionClick() {
        UserDAOImpl userDAO = new UserDAOImpl();
        userDAO.updateSessionUser();
        // Reload the data based on the new session user, if needed
        // For example, you can call initialize method again
        initialize(null, null);
    }

}
