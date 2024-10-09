package jm.task.core.jdbc.util;

import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
	private static final Logger logger = Logger.getLogger(Util.class.getName());
	
	private static final String URL = "jdbc:mysql://localhost:3306/jpp_1_1?useSSL=false&serverTimezone=UTC";
	private static final String USERNAME = "Maxim Muzhikov";
	private static final String PASSWORD = "12345Aa!";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
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
}
