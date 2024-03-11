package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id BIGSERIAL NOT NULL PRIMARY KEY,
                firstName VARCHAR(100) NOT NULL,
                lastName VARCHAR(150) NOT NULL,
                age SMALLINT NOT NULL
                );
                """;

        try (var connection = Util.get();
             var statement = connection.createStatement()
        ) {
//            if (statement.execute(sql)) {
//                System.out.println("Таблица создана");

            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Ошибка при попытке создания таблицы users");
        }
    }

    public void dropUsersTable() {
        String sql = """
                DROP TABLE IF EXISTS users
                """;

        try (var connection = Util.get();
             var statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Ошибка при попытке удаления таблицы users");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = """
                INSERT INTO users (firstName, lastName, age)
                VALUES (?,?,?)
                """;

        try (var connection = Util.get();
             var preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            System.out.println(name + " добавлен в users");
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении " + name + " в users");
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (var connection = Util.get();
             var preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Удалена строка с ID: " + id + " из users");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении строки с ID: " + id + " из users");
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = """
                SELECT id, firstName, lastName, age
                FROM users
                """;

        try (var connection = Util.get();
             var statement = connection.createStatement()
        ) {
            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка всех пользователей из users");
        }

        return users;
    }

    public void cleanUsersTable() {
        String sql = """
                TRUNCATE TABLE users;
                                
                ALTER SEQUENCE users_id_seq
                RESTART WITH 1
                """;

        try (var connection = Util.get();
             var statement = connection.createStatement()
        ) {
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println("Ошибка при попытке очистки таблицы users");
        }
    }
}
