package projectuser;

import project.Project;
import user.User;

import java.util.List;
/**
 * Этот интерфейс обеспечивает операции CRUD, связанные с пользовательскими сущностями проекта.
 * Пользователь проекта - это пользователь, участвующий в проекте с определенной ролью.
 * Author: Дмитрий Куков
 * Date: 28.05.2024
 */
public interface ProjectUserDAO {
    List<Project> getUserProjects(int userId);
    void addProjectUser(ProjectUser projectUser);
    void deleteProjectUser(ProjectUser projectUser);
    boolean checkUserInProject(int userId);
    int countUsersInProject(int projectId);
    void deleteProjectUserByProjectId(int projectId);
    void updateProjectUser(ProjectUser projectUser);
    String getUserRoleInProject(int projectId, int userId);
}
