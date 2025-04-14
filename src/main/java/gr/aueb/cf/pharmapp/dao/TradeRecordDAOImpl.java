package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyDAOException;
import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


import java.time.LocalDateTime;
import java.util.List;

public class TradeRecordDAOImpl implements ITradeRecordDAO {

    private final EntityManagerFactory emf;

    public TradeRecordDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public TradeRecord save(TradeRecord tradeRecord) throws TradeRecordDAOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;

        try{
            tx = em.getTransaction();
            tx.begin();
            em.persist(tradeRecord);
            tx.commit();
            return tradeRecord;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new TradeRecordDAOException("Error saving trade record: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public TradeRecord update(TradeRecord tradeRecord) throws TradeRecordDAOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            TradeRecord merged = em.merge(tradeRecord);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new TradeRecordDAOException("Error updating trade record: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long id) throws TradeRecordDAOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            TradeRecord tradeRecord = em.find(TradeRecord.class, id);
            if(tradeRecord != null){
                em.remove(tradeRecord);
            }
            tx.commit();
            return;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new TradeRecordDAOException("Error deleting trade record: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }

    @Override
    public TradeRecord getById(Long id) throws TradeRecordDAOException {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(TradeRecord.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<TradeRecord> getAll() throws TradeRecordDAOException {
        EntityManager em = emf.createEntityManager();

        try{
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TradeRecord> query = cb.createQuery(TradeRecord.class);
            Root<TradeRecord> tr = query.from(TradeRecord.class);

            query.select(tr);

            List<TradeRecord> tradeRecords = em.createQuery(query).getResultList();

            return tradeRecords;

        }catch (Exception e) {

            throw new TradeRecordDAOException("Error retrieving all trade " +
                    "records: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
