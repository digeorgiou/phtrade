package gr.aueb.cf.pharmapp.dao;


import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.exceptions.UserNotFoundException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.security.SecurityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;

import java.util.List;

public class UserDAOImpl implements IUserDAO{

    private final EntityManagerFactory emf;

    public UserDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User save(User user) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new UserDAOException("Error saving user");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User update(User user) throws UserDAOException {

        //checking if password was changed
        User existingUser = getById(user.getId());
        if(!user.getPassword().equals(existingUser.getPassword())){
            //if password was changed - hash the new one
            String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
        }


        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            User merged = em.merge(user);
            tx.commit();
            return merged;
        } catch (Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
            throw new UserDAOException("Error in updating user " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long userIdToDelete, Long loggedInUserId) throws UserDAOException {

        if (!isAdmin(loggedInUserId) || loggedInUserId.equals(userIdToDelete)) {
            throw new UserDAOException("Unauthorized deletion attempt");
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            User user = em.find(User.class, userIdToDelete);
            if (user != null) {
                em.remove(user);
            }
            tx.commit();
        } catch (Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
            throw new UserDAOException("Error in deleting user " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<User> getAll() throws UserDAOException {
        EntityManager em = emf.createEntityManager();

        try{

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> user = query.from(User.class);

            query.select(user);

            List<User> users = em.createQuery(query).getResultList();

            return users;

        }catch (Exception e) {

            throw new UserDAOException("Error retrieving all users " +
                    "records: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public User getById(Long id) throws UserDAOException{
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(User.class,id);
        } catch (Exception e){
            throw new UserDAOException("Error in finding user by id" + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User getByUsername(String username) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        try{

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> user = query.from(User.class);

            // Eagerly fetch pharmacies and contacts
            Fetch<User, Pharmacy> pharmaciesFetch = user.fetch("pharmacies", JoinType.LEFT);

            // Eagerly fetch contacts and their associated pharmacy
            Fetch<User, PharmacyContact> contactsFetch = user.fetch("contacts", JoinType.LEFT);
            contactsFetch.fetch("pharmacy", JoinType.LEFT);

            query.select(user)
                    .where(cb.equal(user.get("username"), username));

            User result = em.createQuery(query).getSingleResult();

            return result;

        }catch (Exception e) {

            throw new UserDAOException("Error retrieving user by username: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean isUserValid(String username, String password)
            throws UserDAOException, UserNotFoundException {

        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Attempting to validate user: " + username);

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root)
                    .where(cb.equal(root.get("username"), username));

            try {
                User user = em.createQuery(query).getSingleResult();
                System.out.println("Found user: " + user.getUsername());

                // Debug password comparison
                System.out.println("Input password: " + password);
                System.out.println("Stored hash: " + user.getPassword());
                System.out.println("Comparison result: " +
                        SecurityUtil.isPasswordValid(password, user.getPassword()));

                return SecurityUtil.isPasswordValid(password, user.getPassword());

            } catch (NoResultException e) {
                System.out.println("User not found: " + username);
                throw new UserNotFoundException("User not found with username " + username);
            }

        } catch (Exception e) {
            System.out.println("Validation error: " + e.getMessage());
            throw new UserDAOException("Error validating user credentials");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

//    @Override
//    public boolean isUserValid(String username, String password) throws UserDAOException, UserNotFoundException {
//        EntityManager em = emf.createEntityManager();
//        try {
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<User> query = cb.createQuery(User.class);
//            Root<User> root = query.from(User.class);
//
//            query.select(root)
//                    .where(cb.equal(root.get("username"),username));
//
//            try {
//                User user = em.createQuery(query).getSingleResult();
//
//                return SecurityUtil.isPasswordValid(password, user.getPassword());
//            } catch (Exception e) {
//                throw new UserNotFoundException("User not found with username" +
//                        " " + username);
//            }
//
//        } catch (Exception e) {
//            throw new UserDAOException("Error validating user credentials " + e);
//        } finally {
//            if (em != null && em.isOpen()) {
//                em.close();
//            }
//        }
//    }

    @Override
    public boolean isAdmin(Long userId) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, userId);
            return user != null && user.getRoleType() == RoleType.ADMIN;
        } catch (Exception e) {
            throw new UserDAOException("Error checking admin status " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User findByPharmacy(Pharmacy pharmacy) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root)
                    .where(cb.equal(root.get("pharmacy"), pharmacy));

            return em.createQuery(query).getSingleResult();

        } catch (Exception e) {
            throw new UserDAOException("Error finding user by pharmacy " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean usernameExists(String username) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<User> root = query.from(User.class);

            query.select(cb.count(root))
                    .where(cb.equal(root.get("username"), username));

            return em.createQuery(query).getSingleResult() > 0;

        } catch (Exception e) {
            throw new UserDAOException("Error checking username existence " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean emailExists(String email) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<User> root = query.from(User.class);

            query.select(cb.count(root))
                    .where(cb.equal(root.get("email"), email));

            return em.createQuery(query).getSingleResult() > 0;

        } catch (Exception e) {
            throw new UserDAOException("Error checking email existence " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
