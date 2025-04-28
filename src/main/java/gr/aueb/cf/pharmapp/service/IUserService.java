package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.dto.UserInsertDTO;
import gr.aueb.cf.pharmapp.dto.UserLoginDTO;
import gr.aueb.cf.pharmapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.pharmapp.dto.UserUpdateDTO;
import gr.aueb.cf.pharmapp.exceptions.*;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.User;

import java.util.List;

public interface IUserService {

    UserReadOnlyDTO insertUser(UserInsertDTO dto) throws UserDAOException, UserAlreadyExistsException;
    UserReadOnlyDTO updateUser(Long id, UserUpdateDTO dto) throws UserNotFoundException, UserAlreadyExistsException, UserDAOException;
    void deleteUser(Long userIdToDelete, Long loggedInUserId) throws UserNotFoundException, UserAnauthorizedException;
    UserReadOnlyDTO getUserById(Long id) throws UserDAOException,
            UserNotFoundException;
    List<UserReadOnlyDTO> getAllUsers() throws UserDAOException;
    UserReadOnlyDTO getUserByUsername(String username) throws UserNotFoundException,
            UserDAOException;
    List<PharmacyContact> getUserContactsWithPharmacies(Long userId) throws UserDAOException;
    boolean authenticate(UserLoginDTO userLoginDTO)  throws UserNotFoundException, UserDAOException;
    boolean usernameExists(String username) throws UserDAOException;
    boolean emailExists(String email) throws UserDAOException;
    UserReadOnlyDTO assignUserToPharmacy(Long userId, Long pharmacyId, Long adminUserId)
            throws UserNotFoundException, PharmacyNotFoundException, UserAnauthorizedException,
            UserDAOException;
    UserReadOnlyDTO unassignUserFromPharmacy(Long userId, Long adminUserId,
                                             Long pharmacyId)
            throws UserNotFoundException, UserAnauthorizedException,
            PharmacyNotFoundException,
            UserDAOException;

}
