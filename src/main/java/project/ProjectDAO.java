package project;

import java.util.List;
/**
 * Этот интерфейс представляет собой объект доступа к данным (DAO) для объектов проекта.
 * Он предоставляет методы для доступа к объектам проекта в базе данных и управления ими.
 * Author: Дмитрий Куков
 * Date: 28.05.2024
 */
public interface ProjectDAO {
    List<Project> getAllProjects();
    Project getProjectById(int id);
    void addProject(Project project);
    void updateProject(Project project);
    void deleteProject(int id);
    int getProjectIdByName(String projectName);
    void addLinkGitHub(LinkGitHub linkGitHub);
    LinkGitHub getLinkGitHubByProjectId(int projectId);
    void deleteLinkGitHub(LinkGitHub linkGitHub);
}
