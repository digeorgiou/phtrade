package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.dao.*;
import gr.aueb.cf.pharmapp.dto.TradeRecordInsertDTO;
import gr.aueb.cf.pharmapp.dto.TradeRecordReadOnlyDTO;
import gr.aueb.cf.pharmapp.dto.TradeRecordUpdateDTO;
import gr.aueb.cf.pharmapp.exceptions.*;
import gr.aueb.cf.pharmapp.mapper.Mapper;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import gr.aueb.cf.pharmapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import javax.xml.transform.TransformerException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TradeRecordServiceImpl implements ITradeRecordService{

    private final ITradeRecordDAO tradeRecordDAO;
    private final IPharmacyDAO pharmacyDAO;
    private final IUserDAO userDAO;
    private final EntityManagerFactory emf;

    public TradeRecordServiceImpl(EntityManagerFactory emf) {
        this.tradeRecordDAO = new TradeRecordDAOImpl(emf);
        this.pharmacyDAO = new PharmacyDAOImpl(emf);
        this.userDAO = new UserDAOImpl(emf);
        this.emf = emf;
    }

    @Override
    public TradeRecordReadOnlyDTO create(TradeRecordInsertDTO dto) throws TradeRecordDAOException, TradeRecordAlreadyExistsException,
            PharmacyNotFoundException,PharmacyDAOException, UserAnauthorizedException, UserDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;

        try{
            tx = em.getTransaction();
            tx.begin();

            // Get required entities
            Pharmacy giver = pharmacyDAO.getById(dto.getGiverPharmacyId());
            Pharmacy receiver = pharmacyDAO.getById(dto.getReceiverPharmacyId());
            User recorder = userDAO.getById(dto.getRecorderUserId());

            if(giver == null || receiver == null) {
                throw new PharmacyNotFoundException("Giver or receiver " +
                        "pharmacy not found");
            }

            if (recorder == null){
                throw new UserAnauthorizedException("recorder user not found");
            }

            // Verify recorder is associated with one of the pharmacies or is admin
            boolean isAdmin = recorder.getRoleType() == RoleType.ADMIN;
            boolean isGiverUser = giver.getUser() != null && giver.getUser().equals(recorder);
            boolean isReceiverUser = receiver.getUser() != null && receiver.getUser().equals(recorder);

            if (!isAdmin && !isGiverUser && !isReceiverUser) {
                throw new UserAnauthorizedException("Only pharmacy users or admin can create " +
                        "records");
            }

            // Create and save record
            TradeRecord record = Mapper.mapTradeRecordInsertToModel(dto, giver, receiver, recorder);

            record = tradeRecordDAO.save(record);

            // Update pharmacy relationships
            giver.addRecordGiver(record);
            receiver.addRecordReceiver(record);
            pharmacyDAO.update(giver);
            pharmacyDAO.update(receiver);

            tx.commit();
            return Mapper.mapTradeRecordToReadOnlyDTO(record).orElse(null);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new TradeRecordDAOException("Error creating trade record: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public TradeRecordReadOnlyDTO update(TradeRecordUpdateDTO dto) throws TradeRecordDAOException,
            TradeRecordAlreadyExistsException, TradeRecordNotFoundException, PharmacyNotFoundException,PharmacyDAOException,
            UserAnauthorizedException, UserDAOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            TradeRecord existing = tradeRecordDAO.getById(dto.getId());
            if (existing == null) {
                throw new TradeRecordNotFoundException("Trade record not found");
            }

            User updater = userDAO.getById(dto.getUpdaterUserId());
            if (updater == null) {
                throw new UserDAOException("Updater user not found");
            }

            // Verify updater is giver, receiver, or admin
            boolean isAdmin = updater.getRoleType() == RoleType.ADMIN;
            boolean isGiverUser = existing.getGiver().getUser() != null &&
                    existing.getGiver().getUser().equals(updater);
            boolean isReceiverUser = existing.getReceiver().getUser() != null &&
                    existing.getReceiver().getUser().equals(updater);

            if (!isAdmin && !isGiverUser && !isReceiverUser) {
                throw new UserAnauthorizedException("Only giver, receiver,  or admin can " +
                        "update records");
            }

            // Get updated pharmacies if changed
            Pharmacy giver = existing.getGiver();
            Pharmacy receiver = existing.getReceiver();

            if (dto.getGiverPharmacyId() != null && !dto.getGiverPharmacyId().equals(giver.getId())) {
                giver = pharmacyDAO.getById(dto.getGiverPharmacyId());
                if (giver == null) throw new PharmacyNotFoundException("New giver pharmacy not found");
            }

            if (dto.getReceiverPharmacyId() != null && !dto.getReceiverPharmacyId().equals(receiver.getId())) {
                receiver = pharmacyDAO.getById(dto.getReceiverPharmacyId());
                if (receiver == null) throw new PharmacyNotFoundException("New receiver pharmacy not found");
            }

            // Update record
            TradeRecord updated = Mapper.mapTradeRecordUpdateToModel(dto,
                    existing, giver, receiver, updater);
            updated.setLastModifiedBy(updater);
            updated = tradeRecordDAO.update(updated);

            tx.commit();
            return Mapper.mapTradeRecordToReadOnlyDTO(updated).orElse(null);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new TradeRecordDAOException("Error updating trade record: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public void delete(Long id, Long deleterUserId, Boolean isGiverSide) throws TradeRecordDAOException,
            TradeRecordNotFoundException, UserDAOException,UserAnauthorizedException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            TradeRecord record = tradeRecordDAO.getById(id);
            if (record == null) {
                throw new TradeRecordNotFoundException("Trade record not found");
            }

            User deleter = userDAO.getById(deleterUserId);
            if (deleter == null) {
                throw new UserDAOException("Deleter user not found");
            }

            boolean isAdmin = deleter.getRoleType() == RoleType.ADMIN;
            boolean isGiverUser = record.getGiver().getUser() != null &&
                    record.getGiver().getUser().equals(deleter);
            boolean isReceiverUser = record.getReceiver().getUser() != null &&
                    record.getReceiver().getUser().equals(deleter);

            if (!isAdmin && !isGiverUser && !isReceiverUser) {
                throw new UserAnauthorizedException("Only giver, receiver, recorder or admin can " +
                        "delete records");
            }

            // Two-phase deletion logic
            if (isGiverSide) {
                record.setDeletedByGiver(true);
            } else {
                record.setDeletedByReceiver(true);
            }

            // Check if both parties have marked for deletion
            if (record.isDeletedByGiver() && record.isDeletedByReceiver()) {
                // Actually delete the record
                tradeRecordDAO.delete(id);
            } else {
                // Just update the deletion flags
                tradeRecordDAO.update(record);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new TradeRecordDAOException("Error deleting trade record: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public TradeRecordReadOnlyDTO getById(Long id) throws TradeRecordDAOException, TradeRecordNotFoundException {
        TradeRecord tradeRecord;

        try {
            tradeRecord = tradeRecordDAO.getById(id);
            return Mapper.mapTradeRecordToReadOnlyDTO(tradeRecord)
                    .orElseThrow(()-> new TradeRecordNotFoundException("Trade" +
                            " Record with id " + id + " was not found"));
        } catch(TradeRecordDAOException | TradeRecordNotFoundException e) {
            throw e;
        }
    }

    @Override
    public List<TradeRecordReadOnlyDTO> getAll() throws TradeRecordDAOException {

        List<TradeRecord> tradeRecords;
        try {
            tradeRecords = tradeRecordDAO.getAll();
            return tradeRecords.stream()
                    .map(Mapper::mapTradeRecordToReadOnlyDTO)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (TradeRecordDAOException e) {
            throw new TradeRecordDAOException("Error retrieving active trade records");
        }

    }

    // Additional business methods

    /**
     * Get recent trades for a pharmacy (with pagination)
     * @param pharmacyId The pharmacy ID
     * @param limit Maximum number of records to return
     * @return List of recent trade DTOs
     */


    @Override
    public List<TradeRecordReadOnlyDTO> getRecentTradesForPharmacy(Long pharmacyId, int limit) throws PharmacyNotFoundException,
            PharmacyDAOException, TradeRecordDAOException {
        Pharmacy pharmacy = pharmacyDAO.getById(pharmacyId);
        if (pharmacy == null) {
            throw new PharmacyNotFoundException("Pharmacy not found with ID: " + pharmacyId);
        }

        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TradeRecord> query = cb.createQuery(TradeRecord.class);
            Root<TradeRecord> trade = query.from(TradeRecord.class);

            // Get trades where pharmacy is either giver or receiver
            Predicate isGiver = cb.equal(trade.get("giver"), pharmacy);
            Predicate isReceiver = cb.equal(trade.get("receiver"), pharmacy);
            query.where(cb.or(isGiver, isReceiver))
                    .orderBy(cb.desc(trade.get("transactionDate")));

            List<TradeRecord> records = em.createQuery(query)
                    .setMaxResults(limit)
                    .getResultList();

            return records.stream()
                    .map(Mapper::mapTradeRecordToReadOnlyDTO)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new TradeRecordDAOException("Error fetching recent trades: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Get all trades between two pharmacies in a time period
     * @param pharmacy1Id First pharmacy ID
     * @param pharmacy2Id Second pharmacy ID
     * @param startDate Start of time period (inclusive)
     * @param endDate End of time period (inclusive)
     * @return List of trade DTOs between the pharmacies
     */

    @Override
    public List<TradeRecordReadOnlyDTO> getTradesBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id, LocalDateTime startDate, LocalDateTime endDate)
            throws PharmacyNotFoundException, PharmacyDAOException,
            TradeRecordDAOException {
        Pharmacy pharmacy1 = pharmacyDAO.getById(pharmacy1Id);
        Pharmacy pharmacy2 = pharmacyDAO.getById(pharmacy2Id);
        if (pharmacy1 == null || pharmacy2 == null) {
            throw new PharmacyNotFoundException("One or both pharmacies not found");
        }

        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TradeRecord> query = cb.createQuery(TradeRecord.class);
            Root<TradeRecord> trade = query.from(TradeRecord.class);

            // Trades where:
            // (giver=pharmacy1 AND receiver=pharmacy2) OR (giver=pharmacy2 AND receiver=pharmacy1)
            // AND transactionDate between start and end dates
            Predicate giver1Receiver2 = cb.and(
                    cb.equal(trade.get("giver"), pharmacy1),
                    cb.equal(trade.get("receiver"), pharmacy2)
            );

            Predicate giver2Receiver1 = cb.and(
                    cb.equal(trade.get("giver"), pharmacy2),
                    cb.equal(trade.get("receiver"), pharmacy1)
            );

            Predicate dateRange = cb.between(
                    trade.get("transactionDate"),
                    startDate,
                    endDate
            );

            query.where(cb.and(
                    cb.or(giver1Receiver2, giver2Receiver1),
                    dateRange
            )).orderBy(cb.asc(trade.get("transactionDate")));

            List<TradeRecord> records = em.createQuery(query).getResultList();

            return records.stream()
                    .map(Mapper::mapTradeRecordToReadOnlyDTO)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new TradeRecordDAOException("Error fetching trades between pharmacies: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Calculate balance between two pharmacies (pharmacy1's perspective)
     * @param pharmacy1Id First pharmacy ID
     * @param pharmacy2Id Second pharmacy ID
     * @return Balance amount (positive = pharmacy1 owes pharmacy2, negative = pharmacy2 owes pharmacy1)
     */

    @Override
    public Double calculateBalanceBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id) throws PharmacyNotFoundException,
            PharmacyDAOException, TradeRecordDAOException {
        Pharmacy pharmacy1 = pharmacyDAO.getById(pharmacy1Id);
        Pharmacy pharmacy2 = pharmacyDAO.getById(pharmacy2Id);
        if (pharmacy1 == null || pharmacy2 == null) {
            throw new PharmacyNotFoundException("One or both pharmacies not found");
        }

        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // Query for sum of amounts where pharmacy1 is giver
            Double givenSum = getSumForDirection(em, cb, pharmacy1, pharmacy2);

            // Query for sum of amounts where pharmacy1 is receiver
            Double receivedSum = getSumForDirection(em, cb, pharmacy2,
                    pharmacy1);

            // Balance = received - given
            return receivedSum - givenSum;

        } catch (Exception e) {
            throw new TradeRecordDAOException("Error calculating balance: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private Double getSumForDirection(EntityManager em, CriteriaBuilder cb,
                                      Pharmacy giver, Pharmacy receiver) {
        CriteriaQuery<Double> sumQuery = cb.createQuery(Double.class);
        Root<TradeRecord> trade = sumQuery.from(TradeRecord.class);

        sumQuery.select(cb.sum(trade.get("amount")))
                .where(cb.and(
                        cb.equal(trade.get("giver"), giver),
                        cb.equal(trade.get("receiver"), receiver)
                ));

        Double result = em.createQuery(sumQuery).getSingleResult();
        return result != null ? result : 0;
    }
}
