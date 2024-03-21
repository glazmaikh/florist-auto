package api;

import entityDB.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class PartnerProfileDao {
    public UserEntity getPartnerInfo(Long accountId) {
        try (Session session = HibernateUtil.getFlowersSession()) {
            Query<UserEntity> query = session.createQuery("FROM UserEntity WHERE account_id = :accountId", UserEntity.class);
            query.setParameter("accountId", accountId);
            List<UserEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AccountEntity getSupplierInfo(Long accountId) {
        try (Session session = HibernateUtil.getFlowersSession()) {
            Query<AccountEntity> query = session.createQuery("FROM AccountEntity WHERE id = :accountId", AccountEntity.class);
            query.setParameter("accountId", accountId);
            List<AccountEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserPSEntity getUserPSInfo(Long supplierId) {
        try (Session session = HibernateUtil.getPsSession()) {
            Query<UserPSEntity> query = session.createQuery("FROM entityDB.UserPSEntity WHERE supplier_id = :supplierId", UserPSEntity.class);
            query.setParameter("supplierId", supplierId);
            List<UserPSEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LegalEntity getLegal(Long accountId) {
        try (Session session = HibernateUtil.getFlowersSession()) {
            Query<LegalEntity> query = session.createQuery("FROM LegalEntity WHERE account_id = :accountId", LegalEntity.class);
            query.setParameter("accountId", accountId);
            List<LegalEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BankEntity getBank(Long accountId) {
        try (Session session = HibernateUtil.getFlowersSession()) {
            Query<BankEntity> query = session.createQuery("FROM BankEntity WHERE account_id = :accountId", BankEntity.class);
            query.setParameter("accountId", accountId);
            List<BankEntity> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<WorkTimeEntity> getWorkTime(Long accountId) {
        try (Session session = HibernateUtil.getFlowersSession()) {
            Query<WorkTimeEntity> query = session.createQuery("FROM WorkTimeEntity WHERE account_id = :accountId", WorkTimeEntity.class);
            query.setParameter("accountId", accountId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}