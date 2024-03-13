package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Hib_Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = Hib_Util.getSessionFactory();
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id BIGSERIAL NOT NULL PRIMARY KEY,
                firstName VARCHAR(100) NOT NULL,
                lastName VARCHAR(150) NOT NULL,
                age SMALLINT NOT NULL
                );
                """;

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            checkTransaction(transaction);
            throw new RuntimeException("Не удалось создать таблицу", e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = """
                DROP TABLE IF EXISTS users
                """;

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            checkTransaction(transaction);
            throw new RuntimeException("Не удалось удалить таблицу", e);
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
        } catch (Exception e) {
            checkTransaction(transaction);
            throw new RuntimeException("Не удалось сохранить пользователя", e);
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
            }
            transaction.commit();
        } catch (Exception e) {
            checkTransaction(transaction);
            throw new RuntimeException("Не удалось удалить пользователя", e);
        }

    }

    @Override
    public List<User> getAllUsers() {

        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить список пользователей", e);
        }
    }

    @Override
    public void cleanUsersTable() {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            checkTransaction(transaction);
            throw new RuntimeException("Не удалось очистить таблицу", e);
        }

    }

    public void checkTransaction(Transaction t) {
        if (t != null && t.isActive()) {
            t.rollback();
        }
    }



}
