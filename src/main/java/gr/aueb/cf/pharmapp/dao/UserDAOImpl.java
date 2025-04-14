package gr.aueb.cf.pharmapp.dao;


import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.security.SecurityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class UserDAOImpl implements IUserDAO{

    private final EntityManagerFactory emf;

    public UserDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User save(User user) throws UserDAOException {

        String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            em.persist(user);
            tx.commit();
            return user;
        } catch (Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
            throw new UserDAOException("Error in saving user " + e.getMessage());
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
        EntityTransaction tx = em.getTransaction();

        try{
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
        EntityTransaction tx = em.getTransaction();

        try{
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

        try {
            return em.createQuery("Select u FROM User u", User.class).getResultList();
        } finally {
            if(em != null && em.isOpen()){
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
            return em.createQuery("SELECT u FROM User u WHERE u.username = " +
                    ":username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e){
            throw new UserDAOException("Error in finding user by username" + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean isUserValid(String username, String password) throws UserDAOException {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return user != null && SecurityUtil.isPasswordValid(password,
                    user.getPassword());
        } catch (Exception e) {
            throw new UserDAOException("Error validating user credentials " + e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

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
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.pharmacy = :pharmacy", User.class)
                    .setParameter("pharmacy", pharmacy)
                    .getSingleResult();
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
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new UserDAOException("Error checking username existence " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
