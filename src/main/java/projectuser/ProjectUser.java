package projectuser;
/**
 * Класс, представляющий участника проекта.
 * Содержит информацию о роли участника в проекте.
 * Автор: Дмитрий Куков
 * Дата: 28.05.2024
 */
public class ProjectUser {
    private int projectId;
    private int userId;
    private String role;

    public ProjectUser(int projectId, int userId, String role) {
        this.projectId = projectId;
        this.userId = userId;
        this.role = role;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}