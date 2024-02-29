package api;

import entityDB.LegalEntity;
import entityDB.UserEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class PartnerProfileDao {
    public UserEntity getUserInfo(Long accountId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<UserEntity> query = session.createQuery("FROM UserEntity WHERE account_id = :accountId", UserEntity.class);
            query.setParameter("accountId", accountId);
            List<UserEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LegalEntity getLegal(Long accountId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<LegalEntity> query = session.createQuery("FROM LegalEntity WHERE account_id = :accountId", LegalEntity.class);
            query.setParameter("accountId", accountId);
            List<LegalEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}