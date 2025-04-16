package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
    public List<Pharmacy> getByName(String name) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try{

                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
                Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

                query.select(pharmacy)
                        .where(cb.like(pharmacy.get("name"),"%" + name + "%"));

                List<Pharmacy> pharmacies = em.createQuery(query).getResultList();

                return pharmacies;


            }catch (Exception e) {

            throw new PharmacyDAOException("Error retrieving pharmacy by " +
                    "name: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Pharmacy findByUser(User user) throws PharmacyDAOException {
        EntityManager em = emf.createEntityManager();
        try{

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pharmacy> query = cb.createQuery(Pharmacy.class);
            Root<Pharmacy> pharmacy = query.from(Pharmacy.class);

            query.select(pharmacy)
                    .where(cb.equal(pharmacy.get("user"),user));

            Pharmacy result = em.createQuery(query).getSingleResult();

            return result;

        }catch (Exception e) {

            throw new PharmacyDAOException("Error retrieving pharmacy by " +
                    "user: " + e.getMessage());
        }finally {
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
