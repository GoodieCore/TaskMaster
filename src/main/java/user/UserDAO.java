package user;

import project.Project;
/**
 * Класс представляет интерфейс для взаимодействия с пользователями в системе.
 * Автор: Дмитрий Куков
 * Дата: 28.05.2024
 */
public interface UserDAO {
    User getUserById(int id);
    void addUser(User user);
    void updateSessionUser();
    int getCurrentUserId();
    User findUserByDetails(String name, String surname, String group);
    void updateCurrentUserId(int userId);
}
