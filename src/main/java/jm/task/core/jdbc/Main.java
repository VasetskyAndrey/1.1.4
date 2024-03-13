package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Hib_Util;

public class Main {
    public static void main(String[] args) {

        // реализуйте алгоритм здесь
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Ivan", "Ivanov", (byte) 20);
        userService.saveUser("Petr", "Petrov", (byte) 25);
        userService.saveUser("Alexey", "Alexeev", (byte) 30);
        userService.saveUser("Dmitry", "Dmitriev", (byte) 35);

        userService.removeUserById(1);
        System.out.println(userService.getAllUsers());
//        userService.cleanUsersTable();

//        userService.dropUsersTable();

        Hib_Util.closeSessionFactory();
    }
}
