package jm.task.core.jdbc.util;

import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import jm.task.core.jdbc.model.User;

public class Util {
	private static final Logger logger = Logger.getLogger(Util.class.getName());
	
	private static final String URL = "jdbc:mysql://localhost:3306/jpp_1_1?useSSL=false&serverTimezone=UTC";
	private static final String USERNAME = "Maxim Muzhikov";
	private static final String PASSWORD = "12345Aa!";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DIALECT = "org.hibernate.dialect.MySQL8Dialect";

	private static SessionFactory sessionFactory;
	
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			logger.info("Подключение к базе данных установлено!");
			
		} catch (ClassNotFoundException e) {
			logger.error(String.format("Драйвер JDBC не найден: %s", e.getMessage()), e);
			
		} catch (SQLException e) {
			logger.error(String.format("Ошибка SQL: %s", e.getMessage()), e);
		}
		
		return connection;
	}
	
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = getConfiguration();
				configuration.addAnnotatedClass(User.class);

				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties());
				
				sessionFactory = configuration.buildSessionFactory(builder.build());
			} catch (Exception e) {
				System.err.println("Ошибка при создании SessionFactory: " + e.getMessage());
				throw new RuntimeException("Не удалось создать SessionFactory", e);
			}
		}
		return sessionFactory;
	}

	private static Configuration getConfiguration() {
		Configuration configuration = new Configuration();

		Properties settings = new Properties();
		settings.put(Environment.DRIVER, DRIVER);
		settings.put(Environment.URL, URL);
		settings.put(Environment.USER, USERNAME);
		settings.put(Environment.PASS, PASSWORD);
		settings.put(Environment.DIALECT, DIALECT);
		settings.put(Environment.SHOW_SQL, "true");
		//settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		settings.put(Environment.HBM2DDL_AUTO, "create-drop");
		
		configuration.setProperties(settings);
		return configuration;
	}
}
