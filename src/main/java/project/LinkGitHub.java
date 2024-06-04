package project;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * Класс LinkGitHub представляет информацию о ссылке на проект в GitHub,
 * включая идентификатор проекта и ссылку на GitHub.
 * Используется для хранения и работы с информацией о проекте на платформе GitHub.
 **/
public class LinkGitHub {
    private int projectId;
    private String githubLink;

    public LinkGitHub(int projectId, String githubLink) {
        this.projectId = projectId;
        this.githubLink = githubLink;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getGithubLink() {
        return githubLink;
    }

    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }
    /**
     * Переопределенный метод toString() для представления объекта LinkGitHub в виде строки.
     * @return Строковое представление объекта LinkGitHub.
     **/
    @Override
    public String toString() {
        return "LinkGitHub [projectId=" + projectId + ", githubLink=" + githubLink + "]";
    }
    public static boolean isValidGitHubLink(String link) {
        if (link == null || link.isEmpty()) {
            return false;
        }

        try {
            URL url = new URL(link);
            String host = url.getHost();

            // Check if the URL is from GitHub and points to a project
            if (host.equals("github.com")) {
                return true;
            }
        } catch (MalformedURLException e) {
            return false;
        }

        return false;
    }
}
