package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        // реализуйте алгоритм здесь
        try {

            var userService = new UserServiceImpl();

            userService.createUsersTable();
            userService.saveUser("Кот", "Матроскин", (byte) 3);
            userService.saveUser("Пёс", "Шарик", (byte) 2);
            userService.saveUser("Галчонок", "Хватайка", (byte) 0);
            userService.saveUser("Почтальон", "Печкин", (byte) 50);

            System.out.println("-----------------------");
            userService.getAllUsers().forEach(System.out::println);

//        userService.removeUserById(3);
//        userService.cleanUsersTable();
//        userService.dropUsersTable();

        } finally {
            Util.closePull();
        }




    }
}
