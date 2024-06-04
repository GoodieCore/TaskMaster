package user;

import java.sql.*;
import java.util.Scanner;
/**
 * Реализация интерфейса UserDAO для взаимодействия с данными о пользователях в базе данных.
 * Автор: Дмитрий Куков
 * Дата: 28.05.2024
 */
public class UserDAOImpl implements UserDAO{
    private static final String URL = "jdbc:postgresql://localhost:5432/TaskMaster";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "forever4";
    private Connection con;
    public UserDAOImpl() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(con.isClosed());
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или null, если пользователь не найден
     */
    @Override
    public User getUserById(int id) {
        User user = null;
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String group = resultSet.getString("groupus");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String post = resultSet.getString("post");

                user = new User(userId, name, surname, group, phone, email, post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    /**
     * Добавление нового пользователя.
     *
     * @param user Данные нового пользователя.
     */
    @Override
    public void addUser(User user) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO users (name, surname, groupus, phone, email, post)  VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getGroupus());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPost());
            statement.executeUpdate();

            PreparedStatement sessionStatement = con.prepareStatement("UPDATE session SET id_cur_user = ? WHERE id = 1");
            sessionStatement.setInt(1, user.getCurId());
            sessionStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Обновление текущего пользователя в сессии.
     */
    @Override
    public void updateSessionUser() {
        // Code to update session user ID based on input
        try {
            System.out.println("All User Accounts:");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                System.out.println("User ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name"));
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the ID of the user to remember the session: ");
            int selectedUserId = scanner.nextInt();

            PreparedStatement sessionStatement = con.prepareStatement("UPDATE session SET id_cur_user = ? WHERE id = 1");
            sessionStatement.setInt(1, selectedUserId);
            sessionStatement.executeUpdate();

            System.out.println("Session user updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Получение идентификатора текущего пользователя.
     *
     * @return Идентификатор текущего пользователя.
     */
    @Override
    public int getCurrentUserId() {
        int currentUserId = 0;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_cur_user FROM session WHERE id = 1");
            if (resultSet.next()) {
                currentUserId = resultSet.getInt("id_cur_user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentUserId;
    }
    /**
     * Получение последнего идентификатора пользователя.
     *
     * @return Последний идентификатор пользователя.
     */
    public int getLastUserId() {
        int lastUserId = 0;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM users ORDER BY id DESC LIMIT 1");
            if (resultSet.next()) {
                lastUserId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastUserId;
    }
    /**
     * Поиск пользователя по указанным данным.
     *
     * @param name Имя пользователя.
     * @param surname Фамилия пользователя.
     * @param group Группа пользователя.
     * @return Найденный пользователь или null, если не найден.
     */
    @Override
    public User findUserByDetails(String name, String surname, String group) {
        User user = null;
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM users WHERE name = ? AND surname = ? AND groupus = ?");

            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, group);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("name");
                String userSurname = resultSet.getString("surname");
                String userGroup = resultSet.getString("groupus");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String post = resultSet.getString("post");

                user = new User(userId, userName, userSurname, userGroup, phone, email, post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    /**
     * Обновление идентификатора текущего пользователя в сессии.
     *
     * @param userId Новый идентификатор текущего пользователя.
     */
    @Override
    public void updateCurrentUserId(int userId) {
        try {
            PreparedStatement sessionStatement = con.prepareStatement("UPDATE session SET id_cur_user = ? WHERE id = 1");
            sessionStatement.setInt(1, userId);
            sessionStatement.executeUpdate();

            System.out.println("Current user ID updated successfully to: " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
