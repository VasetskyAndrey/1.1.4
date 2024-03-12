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

    // реализуйте настройку соеденения с БД

    public static final String URL_KEY = "db.url";
    public static final String LOGIN_KEY = "db.login";
    public static final String PASSWORD_KEY = "db.password";
    public static final String POOL_SIZE_KEY = "db.pool.size";
    public static final String DEFAULT_POOL_SIZE_KEY = "db.default.pool.size";

    public static BlockingQueue<Connection> pool;
    public static List<Connection> sourceConnections;


    static {
        initConnectionPool();
    }

    private JDBC_Util() {
    }

    private static void initConnectionPool() {

        var poolSize = PropertiesRead_Util.get(POOL_SIZE_KEY);
        var defaultPoolSize = PropertiesRead_Util.get(DEFAULT_POOL_SIZE_KEY);

        var size = poolSize == null ? Integer.parseInt(defaultPoolSize) : Integer.parseInt(poolSize);

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
            return DriverManager.getConnection(
                    PropertiesRead_Util.get(URL_KEY),
                    PropertiesRead_Util.get(LOGIN_KEY),
                    PropertiesRead_Util.get(PASSWORD_KEY)
            );
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
