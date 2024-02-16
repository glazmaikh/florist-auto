package api;

import modelsDB.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class PartnerProfileDao {
    public String getPartnerNameByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            Query<String> query = session.createQuery("SELECT name FROM User WHERE email = :email", String.class);
            query.setParameter("email", email);
            List<String> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getPartnerName() {
        try (Session session = HibernateUtil.getSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> results = query.list();
            return results.isEmpty() ? null : results.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
