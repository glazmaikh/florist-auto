package api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory flowersSessionFactory;
    private static final SessionFactory psSessionFactory;

    static {
        try {
            flowersSessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            psSessionFactory = new Configuration().configure("hibernatePS.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getFlowersSession() {
        return flowersSessionFactory.openSession();
    }

    public static Session getPsSession() {
        return psSessionFactory.openSession();
    }

    public static void shutdown() {
        flowersSessionFactory.close();
        psSessionFactory.close();
    }
}