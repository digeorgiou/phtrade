package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyContactDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class PharmacyContactDAOImpl implements IPharmacyContactDAO {
    private final EntityManagerFactory emf;

    public PharmacyContactDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public PharmacyContact save(PharmacyContact contact) throws PharmacyContactDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            if (contact.getId() == null) {
                em.persist(contact);
            } else {
                contact = em.merge(contact);
            }

            tx.commit();
            return contact;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PharmacyContactDAOException("Error saving pharmacy contact");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long contactId) throws PharmacyContactDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            PharmacyContact contact = em.find(PharmacyContact.class, contactId);
            if (contact != null) {
                em.remove(contact);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PharmacyContactDAOException("Error deleting pharmacy contact");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public PharmacyContact findById(Long id) throws PharmacyContactDAOException {
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(PharmacyContact.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<PharmacyContact> findByUser(Long userId) throws PharmacyContactDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PharmacyContact> query = cb.createQuery(PharmacyContact.class);
            Root<PharmacyContact> root = query.from(PharmacyContact.class);

            query.where(cb.equal(root.get("user").get("id"), userId));
            query.orderBy(cb.asc(root.get("contactName")));

            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new PharmacyContactDAOException("Error finding contacts by user");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean existsByUserAndPharmacy(Long userId, Long pharmacyId) throws PharmacyContactDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<PharmacyContact> root = query.from(PharmacyContact.class);

            query.select(cb.count(root));
            query.where(
                    cb.and(
                            cb.equal(root.get("user").get("id"), userId),
                            cb.equal(root.get("pharmacy").get("id"), pharmacyId)
                    )
            );

            return em.createQuery(query).getSingleResult() > 0;
        } catch (Exception e) {
            throw new PharmacyContactDAOException("Error checking contact existence");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}