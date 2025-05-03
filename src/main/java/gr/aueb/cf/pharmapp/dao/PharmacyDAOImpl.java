package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import gr.aueb.cf.pharmapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;

import java.util.List;

public class PharmacyDAOImpl implements  IPharmacyDAO{

    private final EntityManagerFactory emf;

    public PharmacyDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Pharmacy save(Pharmacy pharmacy) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            // If the pharmacy has a user associated, make sure to maintain the bidirectional relationship
            if (pharmacy.getUser() != null) {
                // Get the managed user entity
                User user = em.find(User.class, pharmacy.getUser().getId());
                if (user != null) {
                    // Add the pharmacy to the user's collection
                    user.getPharmacies().add(pharmacy);
                    em.merge(user); // Ensure the user is updated
                }
            }

            em.persist(pharmacy);
            tx.commit();
            return pharmacy;
        } catch (Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
            throw new PharmacyDAOException("Error in saving pharmacy " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Pharmacy update(Pharmacy pharmacy) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Pharmacy merged = em.merge(pharmacy);
            tx.commit();
            return merged;
        } catch (Exception e){
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PharmacyDAOException("Error updating pharmacy " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long id) throws PharmacyDAOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Pharmacy pharmacy = em.find(Pharmacy.class, id);
            if(pharmacy != null){
                em.remove(pharmacy);
            }
            tx.commit();
        } catch(Exception e){
            if(tx != null && tx.isActive() ){
                tx.rollback();
            }
            throw new PharmacyDAOException("Error deleting pharmacy " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }

    @Override
    public Pharmacy getById(Long id) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(Pharmacy.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Pharmacy> getAll() throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try{

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            query.select(pharmacy);

            List<Pharmacy> pharmacies = em.createQuery(query).getResultList();



            return pharmacies;

        }catch (Exception e) {
            throw new PharmacyDAOException("Error retrieving all pharmacies: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Pharmacy getPharmacyWithRelationsById(Long id) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            // Eagerly fetch all relationships
            pharmacy.fetch("user", JoinType.LEFT);
            pharmacy.fetch("recordsGiver", JoinType.LEFT);
            pharmacy.fetch("recordsReceiver", JoinType.LEFT);
            pharmacy.fetch("contactReferences", JoinType.LEFT);

            query.where(cb.equal(pharmacy.get("id"), id));

            return em.createQuery(query).getSingleResult();
        } catch (Exception e) {
            throw new PharmacyDAOException("Error fetching pharmacy with relations: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Pharmacy getByName(String name) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            pharmacy.fetch("recordsGiver", JoinType.LEFT);
            pharmacy.fetch("recordsReceiver", JoinType.LEFT);
            pharmacy.fetch("contactReferences", JoinType.LEFT);

            query.select(pharmacy)
                    .where(cb.equal(pharmacy.get("name"), name));

            Pharmacy result = em.createQuery(query).getSingleResult();

            return result;


        } catch (Exception e) {
            throw new PharmacyDAOException("Error retrieving pharmacy by name: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Pharmacy> searchPharmaciesByName(String name) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            // Fetch the user relationship
            Fetch<Pharmacy, User> userFetch = pharmacy.fetch("user", JoinType.LEFT);

            // For criteria conditions, create a separate join
            Join<Pharmacy, User> userJoin = pharmacy.join("user", JoinType.LEFT);

            query.where(cb.like(
                    cb.lower(pharmacy.get("name")),
                    "%" + name.toLowerCase() + "%"
            ));

            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new PharmacyDAOException("Error searching pharmacies by name: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Pharmacy> searchPharmaciesByUser(String username) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            // Fetch the user relationship
            Fetch<Pharmacy, User> userFetch = pharmacy.fetch("user", JoinType.LEFT);

            // For criteria conditions, create a separate join
            Join<Pharmacy, User> userJoin = pharmacy.join("user", JoinType.LEFT);

            query.where(cb.like(
                    cb.lower(userJoin.get("username")),
                    "%" + username.toLowerCase() + "%"
            ));

            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new PharmacyDAOException("Error searching pharmacies by user: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    @Override
    public boolean existsByName(String name) throws PharmacyDAOException {

        EntityManager em = emf.createEntityManager();
        try{
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            query.select(cb.count(pharmacy))
                    .where(cb.equal(pharmacy.get("name"),name));

            boolean result = em.createQuery(query).getSingleResult() > 0;

            return result;

        }catch (Exception e) {
            throw new PharmacyDAOException("Error checking if pharmacy " +
                    "exists by Name" +
                    " " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
