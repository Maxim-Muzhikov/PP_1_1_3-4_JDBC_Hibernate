package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDaoHibernateImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class);
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
        logger.info("Создание объекта UserDaoHibernateImpl");
    }
    
    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT not NULL AUTO_INCREMENT, " +
                " name VARCHAR(255), " +
                " lastName VARCHAR(255), " +
                " age TINYINT, " +
                " PRIMARY KEY ( id ))";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            
            statement.executeUpdate(sql);
            logger.info("Таблица users создана");
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при создании таблицы users: %s", e.getMessage()), e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            
            statement.executeUpdate(sql);
            logger.info("Таблица users удалена");
            
        } catch (SQLException e) {
            logger.error(String.format("Ошибка при удалении таблицы users: %s", e.getMessage()), e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);

            session.persist(user);
            transaction.commit();
            
            logger.info(String.format("Пользователь %s %s %d добавлен в базу данных", name, lastName, age));
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(String.format("Ошибка при добавлении пользователя в базу данных: %s", e.getMessage()), e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            
            if (user != null) {
                session.remove(user);
                transaction.commit();
                
                logger.info(String.format("User с id – %d удален из базы данных", id));
            } else {
                logger.warn(String.format("User с id – %d не найден в базе данных", id));
            }
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(String.format("Ошибка при удалении пользователя: %s", e.getMessage()), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            CriteriaQuery<User> all = cq.select(rootEntry);
            
            logger.info("Получен список всех пользователей");
            
            return session.createQuery(all).getResultList();
            
        } catch (Exception e) {
            logger.error(String.format("Ошибка при получении списка пользователей: %s", e.getMessage()), e);
            return new ArrayList<>();
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users").executeUpdate(); // или DELETE FROM users
            transaction.commit();
            
            logger.info("Таблица users очищена");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(String.format("Ошибка при очистке таблицы users: %s", e.getMessage()), e);
        }
    }
}
