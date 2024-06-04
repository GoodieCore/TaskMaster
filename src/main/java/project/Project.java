package project;
/**
 * Представляет проект с его подробными данными, такими как идентификатор, название, срок действия, статус и количество пользователей..
 * Author: Дмитрий Куков
 * Date: 28.05.2024
 */
public class Project {
    private int id;
    private String name;
    private String term;
    private String status;
    private int countUs;


    public int getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }

    public String getTerm() {
        return term;
    }

    public String getName() {
        return name;
    }

    public int getCountUs() {
        return countUs;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setCountUs(int countUs) {
        this.countUs = countUs;
    }
    @Override
    public String toString() {
        return "Project [id=" + id + ", name=" + name + ", term=" + term + ", status=" + status + ", countUs=" + countUs + "]";
    }
}
