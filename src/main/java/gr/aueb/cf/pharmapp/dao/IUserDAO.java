package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.User;

import java.util.List;

public interface IUserDAO {

    User save(User user) throws UserDAOException;
     User update(User user) throws UserDAOException;
    public void delete(Long userIdToDelete, Long loggedInUserId) throws UserDAOException;
     List<User> getAll() throws UserDAOException;
    public User getById(Long id) throws UserDAOException;
    User getByUsername(String username) throws UserDAOException;
    boolean isUserValid(String username, String password) throws UserDAOException;
    public boolean isAdmin(Long userId) throws UserDAOException;
    public User findByPharmacy(Pharmacy pharmacy) throws UserDAOException;
    public boolean usernameExists(String username) throws UserDAOException;
}
