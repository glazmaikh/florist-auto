package api;

import modelsDB.PartnerProfile;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class PartnerProfileDao {
    public PartnerProfile getPartnerProfileByAccountId(Long accountId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<PartnerProfile> query = session.createQuery("FROM PartnerProfile WHERE account_id = :accountId", PartnerProfile.class);
            query.setParameter("accountId", accountId);
            List<PartnerProfile> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}