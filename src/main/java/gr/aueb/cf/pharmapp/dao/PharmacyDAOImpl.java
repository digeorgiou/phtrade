package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class PharmacyDAOImpl implements  IPharmacyDAO{

    private final EntityManagerFactory emf;

    public PharmacyDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Pharmacy save(Pharmacy pharmacy) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
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
        EntityTransaction tx = em.getTransaction();

        try {
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
        EntityTransaction tx = em.getTransaction();

        try{
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
            return em.createQuery("Select p FROM Pharmacy p", Pharmacy.class).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Pharmacy> getByName(String name) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery(
                    "SELECT p FROM Pharmacy p WHERE p.name LIKE :name",
                    Pharmacy.class).setParameter("name", "%" + name + "%").getResultList();
            }finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
    }

    @Override
    public Pharmacy findByUser(User user) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pharmacy p WHERE p.user = :user", Pharmacy.class)
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (Exception e) {
            throw new PharmacyDAOException("Error finding pharmacy by user " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void linkUserToPharmacy(User user, Pharmacy pharmacy) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();

            // Refreshing entities to ensure we're working with managed
            // instances
            User managedUser = em.find(User.class, user.getId());
            Pharmacy managedPharmacy = em.find(Pharmacy.class,
                    pharmacy.getId());


            if(managedUser.getPharmacy() != null){
                managedUser.getPharmacy().setUser(null);
            }
            if(managedPharmacy.getUser() != null){
                managedPharmacy.getUser().setPharmacy(null);
            }

            managedUser.setPharmacy(managedPharmacy);
            managedPharmacy.setUser(managedUser);

            tx.commit();

        } catch(Exception e){
            if(tx != null && tx.isActive() ){
                tx.rollback();
            }
            throw new PharmacyDAOException("Error in linking pharmacy with " +
                    "user " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean existsByName(String name) throws PharmacyDAOException {
        try (EntityManager em = emf.createEntityManager()) {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Pharmacy p WHERE p.name = :name", Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new PharmacyDAOException("Error checking pharmacy existence" +
                    " " + e.getMessage());
        }
    }
}
