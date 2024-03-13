package jm.task.core.jdbc.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class JDBC_Util {

    // реализуйте настройку соединения с БД

    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String LOGIN = "postgres";
    public static final String PASSWORD = "admin";
    public static final String POOL = "5";

    public static BlockingQueue<Connection> pool;
    public static List<Connection> sourceConnections;


    static {
        initConnectionPool();
    }

    private JDBC_Util() {
    }

    private static void initConnectionPool() {

        var size = Integer.parseInt(POOL);

        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {

            var connection = open();
            var proxyConnection = (Connection) Proxy.newProxyInstance(JDBC_Util.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(proxyConnection);

            sourceConnections.add(connection);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {

        try {
            return DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Не удалось получить соединение с БД");
            throw new RuntimeException(e);
        }

    }

    public static void closePull() {
        try {
            for (Connection connect : sourceConnections) {
                connect.close();
            }

            System.out.println("Все открытые соединения были закрыты");
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
