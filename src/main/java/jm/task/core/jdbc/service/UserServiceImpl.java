package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

import java.util.List;
import org.jboss.logging.Logger;

public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    
    public UserServiceImpl() {
        userDao = new UserDaoJDBCImpl();
        logger.info("Создание UserServiceImpl");
    }
    
    public void createUsersTable() {
        userDao.createUsersTable();
        logger.info("Таблица пользователей создана");
    }

    public void dropUsersTable() {
        userDao.dropUsersTable();
        logger.info("Таблица пользователей удалена");
    }

    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
        logger.info(String.format("Пользователь %s %s добавлен", name, lastName));
        System.out.printf("User с именем - %s добавлен в базу данных\n", name);
    }

    public void removeUserById(long id) {
        userDao.removeUserById(id);
        logger.info(String.format("Пользователь с ID %d удален", id));
    }

    public List<User> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        logger.info(String.join("\n", users.stream().map(User::toString).toList()));
        return users;
    }

    public void cleanUsersTable() {
        userDao.cleanUsersTable();
        logger.info("Таблица пользователей очищена");
    }
}
