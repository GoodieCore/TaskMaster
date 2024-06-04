package project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Реализация интерфейса доступа к данным для проекта.
 * Предоставляет методы для взаимодействия с базой данных для управления проектами.
 * Автор: Дмитрий Куков
 * Дата: 28.05.2024
 */
public class ProjectDAOImpl implements ProjectDAO{
    private static final String URL = "jdbc:postgresql://localhost:5432/TaskMaster";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "forever4";
    private Connection con;
    public ProjectDAOImpl() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //System.out.println(con.isClosed());
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * Получить все проекты из базы данных.
     * @return список всех проектов
     */
    @Override
    public List<Project> getAllProjects()
    {
        List<Project> projects= new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from projects");

            while(rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setTerm(rs.getString("term"));
                project.setStatus(rs.getString("status"));
                project.setCountUs(rs.getInt("countUs"));
                //System.out.println(project);

                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
    /**
     * Получить проект по его ID из базы данных.
     * @param id ID проекта для получения
     * @return проект с указанным ID
     */
    @Override
    public Project getProjectById(int id) {
        Project project = new Project();
        try {
            PreparedStatement ps = con.prepareStatement("select * from projects where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setTerm(rs.getString("term"));
                project.setStatus(rs.getString("status"));
                project.setCountUs(rs.getInt("countUs"));
                System.out.println(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }
    /**
     * Добавить новый проект в базу данных.
     * @param project проект для добавления
     */
    @Override
    public void addProject(Project project) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into projects (name, term, status, countUs) values (?, ?, ?, ?)");
            ps.setString(1, project.getName());
            ps.setDate(2, java.sql.Date.valueOf(project.getTerm()));
            ps.setString(3, project.getStatus());
            ps.setInt(4, project.getCountUs());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("Project added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Обновить существующий проект в базе данных.
     * @param project проект для обновления
     */
    @Override
    public void updateProject(Project project) {
        try {
            PreparedStatement ps = con.prepareStatement("update projects set name = ?, term = ?, status = ?, countUs = ? where id = ?");
            ps.setString(1, project.getName());
            ps.setDate(2, java.sql.Date.valueOf(project.getTerm()));
            ps.setString(3, project.getStatus());
            ps.setInt(4, project.getCountUs());
            ps.setInt(5, project.getId());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("Task updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Удалить проект из базы данных по его ID.
     * @param id ID проекта для удаления
     */
    @Override
    public void deleteProject(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from projects where id = ?");
            ps.setInt(1, id);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("Task deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Получить ID проекта по его имени.
     * @param projectName название проекта
     * @return ID проекта с указанным именем
     */
    @Override
    public int getProjectIdByName(String projectName) {
        int projectId = -1;
        try {
            PreparedStatement ps = con.prepareStatement("select id from projects where name = ?");
            ps.setString(1, projectName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                projectId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectId;
    }
    /**
     * Добавить ссылку на GitHub для проекта в базу данных.
     * @param linkGitHub ссылка на GitHub для добавления
     */
    @Override
    public void addLinkGitHub(LinkGitHub linkGitHub) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into linkgithub (project_id, github_link) values (?, ?)");
            ps.setInt(1, linkGitHub.getProjectId());
            ps.setString(2, linkGitHub.getGithubLink());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("GitHub link added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Получить ссылку на GitHub для проекта по его ID.
     * @param projectId ID проекта
     * @return ссылка на GitHub для указанного проекта
     */
    @Override
    public LinkGitHub getLinkGitHubByProjectId(int projectId) {
        LinkGitHub linkGitHub = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM linkgithub WHERE project_id = ?");
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                linkGitHub = new LinkGitHub(projectId, rs.getString("github_link"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return linkGitHub;
    }
    /**
     * Удалить ссылку на GitHub для проекта из базы данных.
     * @param linkGitHub ссылка на GitHub для удаления
     */
    @Override
    public void deleteLinkGitHub(LinkGitHub linkGitHub) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM linkgithub WHERE project_id = ?");
            ps.setInt(1, linkGitHub.getProjectId());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("GitHub link deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }
}
