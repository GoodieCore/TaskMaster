package org.example.taskmaster;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.LinkGitHub;
import project.Project;
import project.ProjectDAOImpl;
import projectuser.ProjectUser;
import projectuser.ProjectUserDAO;
import projectuser.ProjectUserDAOImpl;
import user.User;
import user.UserDAOImpl;
/**
 * Класс FormController отвечает за управление формой проекта,
 * где пользователь может добавить ссылку на GitHub и выбрать свою роль в проекте.
 * Также отображает текущую ссылку на GitHub и роль пользователя в проекте.
 *
 * @author Дмитрий Куков
 * @date 28.05.2024
 **/
public class FormController {
    @FXML
    private Label projectName;
    @FXML
    private TextField textLinkGit;
    @FXML
    private ChoiceBox<String> choiceRole;
    @FXML
    private Button saveGitBtn,saveRoleBtn;
    @FXML
    private Label error,roleText,linkText;
    private Project selectedProject;
    // Define the roles as constants
    private static final String ROLE_LEADER = "Руководитель";
    private static final String ROLE_DEVELOPER = "Разработчик";
    private static final String ROLE_TESTER = "Тестировщик";
    private static final String ROLE_DESIGNER = "Проектировщик";

    /**
     * Устанавливает название проекта в соответствии с выбранным проектом.
     * @param name Название проекта
     */
    public void setProjectName(String name) {
        projectName.setText(name);
    }
    /**
     * Устанавливает выбранный проект.
     * @param project Выбранный проект для редактирования
     */
    public void setSelectedProject(Project project) {
        selectedProject = project;
        displayGitHubLink();
        displayUserProject();
        checkUserProject();
    }
    /**
     * Обработчик события нажатия кнопки для сохранения ссылки на GitHub.
     * Проверяет валидность ссылки и сохраняет её, обновляя статус проекта.
     */
    public void onSaveGitBtnClick() {
        String gitLink = textLinkGit.getText();
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();

        if (LinkGitHub.isValidGitHubLink(gitLink)) {
            LinkGitHub existingLink = projectDAO.getLinkGitHubByProjectId(selectedProject.getId());

            if (existingLink != null) {
                if (!existingLink.getGithubLink().equals(gitLink)) {
                    projectDAO.deleteLinkGitHub(existingLink);
                    existingLink.setGithubLink(gitLink);
                    projectDAO.addLinkGitHub(existingLink);
                }
            } else {
                LinkGitHub newLink = new LinkGitHub(selectedProject.getId(), gitLink);
                projectDAO.addLinkGitHub(newLink);
            }

            // Update project status
            selectedProject.setStatus("В процессе");
            projectDAO.updateProject(selectedProject);

            error.setText("Ссылка добавлена успешно!");
        } else {
            error.setText("Ссылка недействительна!");
        }
    }
    /**
     * Обработчик события нажатия кнопки для сохранения роли пользователя в проекте.
     * Проверяет выбранную роль и сохраняет её для текущего пользователя в проекте.
     */
    public void onSaveRoleBtnClick() {
        String selectedRole = choiceRole.getValue();

        if (selectedRole != null) {
            ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();
            int projectId = selectedProject.getId();
            UserDAOImpl userDAO = new UserDAOImpl();
            int curUserId = userDAO.getCurrentUserId();

            ProjectUser projectUser = new ProjectUser(projectId, curUserId, selectedRole);
            projectUserDAO.updateProjectUser(projectUser);

            // Show success message or perform necessary actions
            error.setText("Выбрана роль: " + selectedRole);
        } else {
            // Handle case when no role is selected
            error.setText("Роль не выбрана");
        }
    }
    /**
     * Инициализация метода настройки контроллера.
     * Добавляет возможные роли в выпадающий список для выбора.
     */
    @FXML
    public void initialize() {
        choiceRole.getItems().addAll(ROLE_LEADER, ROLE_DEVELOPER, ROLE_TESTER, ROLE_DESIGNER);
        //choiceRole.setValue(ROLE_LEADER);
    }
    /**
     * Отображает текущую ссылку на GitHub для выбранного проекта.
     */
    private void displayGitHubLink() {
        if (selectedProject != null) {
            ProjectDAOImpl projectDAO = new ProjectDAOImpl();
            LinkGitHub linkGitHub = projectDAO.getLinkGitHubByProjectId(selectedProject.getId());

            if (linkGitHub != null) {
                String githubLink = linkGitHub.getGithubLink();
                linkText.setText(githubLink);
            } else {
                linkText.setText("Нет ссылки");
            }
        }
    }
    /**
     * Отображает роль текущего пользователя в выбранном проекте.
     */
    public void displayUserProject() {
        UserDAOImpl userDAO = new UserDAOImpl();
        int curUserId = userDAO.getCurrentUserId();

        if (selectedProject != null && curUserId > 0) {
            ProjectUserDAOImpl projectUserDAO = new ProjectUserDAOImpl();
            String userRole = projectUserDAO.getUserRoleInProject(selectedProject.getId(), curUserId);

            if (userRole != null && !userRole.isEmpty()) {
                roleText.setText(userRole);
            } else {
                roleText.setText("Роль не назначена");
            }
        } else {
            roleText.setText("Нет роли");
        }
    }
    /**
     * Проверяет наличие выбранного проекта и доступ пользователя для изменения проекта.
     * Включает или отключает возможность сохранения изменений ссылки на GitHub и роли.
     */
    public void checkUserProject() {
        UserDAOImpl userDAO = new UserDAOImpl();
        int curUserId = userDAO.getCurrentUserId();
        User currentUser = userDAO.getUserById(curUserId);
        if (currentUser != null) {
            if (!"Завершен".equals(selectedProject.getStatus()) && !"Преподаватель".equals(currentUser.getPost())) {
                // Enable saving and changing GitHub link and role
                saveGitBtn.setDisable(false);
                saveRoleBtn.setDisable(false);
            } else {
                // Disable saving and changing GitHub link and role
                saveGitBtn.setDisable(true);
                saveRoleBtn.setDisable(true);
                error.setText("Нельзя изменять проект");
            }
        }
    }
}
