package org.demo.util;

import org.demo.entity.Category;
import org.demo.entity.Order;
import org.demo.entity.OrderDetail;
import org.demo.entity.Product;
import org.demo.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {

            Configuration configuration = new Configuration();

            // Load hibernate.cfg.xml
            configuration.configure();

            // Register entity classes
            configuration.addAnnotatedClass(Category.class);
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(OrderDetail.class);

            // Build SessionFactory
            sessionFactory = configuration.buildSessionFactory();

        } catch (Exception e) {

            e.printStackTrace();

            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}