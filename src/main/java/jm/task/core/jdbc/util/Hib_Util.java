package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hib_Util {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private Hib_Util() {
    }


    private static SessionFactory buildSessionFactory() {

        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "admin");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.addAnnotatedClass(User.class);
        configuration.setProperty("show_sql", "true");

        try {
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить сессию: " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}

