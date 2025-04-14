package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;


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
        EntityTransaction tx = em.getTransaction();

        try{
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
        EntityTransaction tx = em.getTransaction();

        try{
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
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            TradeRecord tradeRecord = em.find(TradeRecord.class, id);
            if(tradeRecord != null){
                em.remove(tradeRecord);
            }
            tx.commit();
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
            return em.createQuery(
                    "SELECT tr FROM TradeRecord tr", TradeRecord.class
            ).getResultList();
        } catch (Exception e) {
            throw new TradeRecordDAOException("Error retrieving all trade records: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<TradeRecord> findTradesBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id,
                                                         LocalDateTime startDate,
                                                         LocalDateTime endDate)
            throws TradeRecordDAOException {

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT t FROM TradeRecord t WHERE " +
                    "((t.giver.id = :pharma1Id AND t.receiver.id = :pharma2Id) OR " +
                    "(t.giver.id = :pharma2Id AND t.receiver.id = :pharma1Id)) AND " +
                    "t.transactionDate BETWEEN :startDate AND :endDate " +
                    "ORDER BY t.transactionDate DESC";

            return em.createQuery(jpql, TradeRecord.class)
                    .setParameter("pharma1Id", pharmacy1Id)
                    .setParameter("pharma2Id", pharmacy2Id)
                    .setParameter("startDate", startDate != null ? startDate : LocalDateTime.MIN)
                    .setParameter("endDate", endDate != null ? endDate : LocalDateTime.MAX)
                    .getResultList();
        } catch (Exception e) {
            throw new TradeRecordDAOException("Error finding trades between pharmacies " + pharmacy1Id +
                    " and " + pharmacy2Id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<TradeRecord> getRecentTrades(Long pharmacyId, int days)
            throws TradeRecordDAOException {

        EntityManager em = emf.createEntityManager();
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);

            String jpql = "SELECT t FROM TradeRecord t WHERE " +
                    "(t.giver.id = :pharmacyId OR t.receiver.id = :pharmacyId) AND " +
                    "t.transactionDate >= :cutoffDate " +
                    "ORDER BY t.transactionDate DESC";

            return em.createQuery(jpql, TradeRecord.class)
                    .setParameter("pharmacyId", pharmacyId)
                    .setParameter("cutoffDate", cutoffDate)
                    .getResultList();
        } catch (Exception e) {
            throw new TradeRecordDAOException("Error finding recent trades for pharmacy " + pharmacyId);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }





}
