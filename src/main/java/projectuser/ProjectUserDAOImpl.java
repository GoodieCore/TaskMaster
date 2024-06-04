package projectuser;

import project.Project;
import user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Реализация интерфейса ProjectUserDAO для работы с данными о пользователях проекта.
 * Дмитрий Куков, 28.05.2024
 */
public class ProjectUserDAOImpl implements ProjectUserDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/TaskMaster";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "forever4";
    private Connection con;
    public ProjectUserDAOImpl() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(con.isClosed());
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * Получает список проектов, связанных с определенным пользователем.
     * @param userId ID пользователя
     * @return Список проектов, связанных с пользователем
     */
    @Override
    public List<Project> getUserProjects(int userId) {
        List<Project> userProjects = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM projects_users pu JOIN projects p ON p.id = pu.project_id WHERE pu.user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setStatus(rs.getString("status"));
                project.setTerm(rs.getString("term"));
                project.setCountUs(rs.getInt("countUs"));
                userProjects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProjects;
    }

    /**
     * Добавляет отображение пользователь-проект в базу данных.
     * @param projectUser Данные о пользователе и проекте
     */
    @Override
    public void addProjectUser(ProjectUser projectUser) {
        String role = "Неизвестен";
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO projects_users (project_id, user_id, role) VALUES (?, ?, ?)");
            ps.setInt(1, projectUser.getProjectId());
            ps.setInt(2, projectUser.getUserId());
            ps.setString(3, role);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("User added to project successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Удаляет отображение пользователь-проект из базы данных.
     * @param projectUser Данные о пользователе и проекте
     */
    @Override
    public void deleteProjectUser(ProjectUser projectUser) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM projects_users WHERE project_id = ? AND user_id = ?");
            ps.setInt(1, projectUser.getProjectId());
            ps.setInt(2, projectUser.getUserId());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("User removed from project successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Проверяет наличие пользователя в проекте.
     * @param userId ID пользователя
     * @return true, если пользователь участвует в проекте, иначе false
     */
    @Override
    public boolean checkUserInProject(int userId) {
        boolean userExists = false;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM projects_users WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userExists;
    }
    /**
     * Подсчитывает количество пользователей в конкретном проекте.
     * @param projectId ID проекта
     * @return Количество пользователей в проекте
     */
    @Override
    public int countUsersInProject(int projectId) {
        int userCount = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS user_count FROM projects_users WHERE project_id = ?");
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userCount = rs.getInt("user_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCount;
    }
    /**
     * Удаляет всех пользователей из проекта по ID проекта.
     * @param projectId ID проекта
     */
    @Override
    public void deleteProjectUserByProjectId(int projectId) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM projects_users WHERE project_id = ?");
            ps.setInt(1, projectId);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("Users removed from project successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Обновляет роль пользователя в конкретном проекте.
     * @param projectUser Данные о пользователе и проекте с обновленной ролью
     */
    @Override
    public void updateProjectUser(ProjectUser projectUser) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE projects_users SET role = ? WHERE project_id = ? AND user_id = ?");
            ps.setString(1, projectUser.getRole());
            ps.setInt(2, projectUser.getProjectId());
            ps.setInt(3, projectUser.getUserId());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("User role updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Получает роль пользователя в конкретном проекте.
     * @param projectId ID проекта
     * @param userId ID пользователя
     * @return Роль пользователя в проекте
     */
    @Override
    public String getUserRoleInProject(int projectId, int userId) {
        String userRole = "Неизвестен";

        try {
            PreparedStatement ps = con.prepareStatement("SELECT role FROM projects_users WHERE project_id = ? AND user_id = ?");
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userRole = rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userRole;
    }
}
