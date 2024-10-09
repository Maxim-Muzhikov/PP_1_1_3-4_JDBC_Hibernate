package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
	public static void main(String[] args) {
		UserServiceImpl userService = new UserServiceImpl();
		
		// Создание таблицы User(ов)
		userService.createUsersTable();
		
		// Добавление 4 User(ов) в таблицу с данными на свой выбор. После каждого добавления должен быть вывод в консоль (User с именем — name добавлен в базу данных)
		userService.saveUser("1", "1", (byte) 1);
		userService.saveUser("2", "2", (byte) 2);
		userService.saveUser("3", "3", (byte) 3);
		userService.saveUser("4", "4", (byte) 4);
		
		// Получение всех User из базы и вывод в консоль (должен быть переопределен toString в классе User)
		userService.getAllUsers().forEach((User user) -> System.out.println(user.toString()));
		
		// Очистка таблицы User(ов)
		userService.cleanUsersTable();
		
		// Удаление таблицы
		userService.dropUsersTable();
	}
}
