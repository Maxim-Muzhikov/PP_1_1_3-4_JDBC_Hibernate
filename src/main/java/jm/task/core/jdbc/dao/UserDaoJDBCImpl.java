package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class);

    public UserDaoJDBCImpl() {
        logger.info("Создание объекта UserDaoJDBCImpl");
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255)," +
                "lastname VARCHAR(255)," +
                "age TINYINT)";
        
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            
            statement.executeUpdate(sql);
            logger.info("Таблица users создана");
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при создании таблицы users: %s", e.getMessage()), e);
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            
            statement.executeUpdate(sql);
            logger.info("Таблица users удалена");
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при удалении таблицы users: %s", e.getMessage()), e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
        
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            logger.info(String.format("Пользователь %s %s %d добавлен в базу данных", name, lastName, age));
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при добавлении пользователя в базу данных: %s", e.getMessage()), e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            
            logger.info(String.format("User с id – %d удален из базы данных", id));
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при удалении пользователя: %s", e.getMessage()), e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
            
            logger.info("Получен список всех пользователей");
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при получении списка пользователей: %s", e.getMessage()), e);
            return new ArrayList<>();
        }
        
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            
            statement.executeUpdate(sql);
            logger.info("Таблица users очищена");
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при очистке таблицы users: %s", e.getMessage()), e);
        }
    }
}
