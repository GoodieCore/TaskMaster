package user;
/**
 * Класс представляет сущность пользователя в системе.
 * Содержит информацию о пользователе, такую как идентификатор, имя, фамилия, группа, телефон, email, должность.
 *  Автор: Дмитрий Куков
 *  Дата: 28.05.2024
 */
public class User {
    private int id;
    private String name;
    private String surname;
    private String groupus;
    private String phone;
    private String email;
    private String post;

    private int curId;

    public User(int id, String name, String surname, String groupus, String phone, String email, String post) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        if(groupus.trim().isEmpty()) {groupus = null;}
        else this.groupus = groupus;
        if(phone.trim().isEmpty()) { phone = null;}
        else this.phone = phone;
        if(email.trim().isEmpty()) {email = null;}
        else this.email = email;
        this.post = post;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGroupus() {
        return groupus;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPost() {
        return post;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroupus(String groupus) {
        this.groupus = groupus;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getCurId() {
        return curId;
    }

    public void setCurId(int curId) {
        this.curId = curId;
    }
}
