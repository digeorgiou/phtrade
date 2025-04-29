package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.dao.IPharmacyDAO;
import gr.aueb.cf.pharmapp.dao.IUserDAO;
import gr.aueb.cf.pharmapp.dao.PharmacyDAOImpl;
import gr.aueb.cf.pharmapp.dao.UserDAOImpl;
import gr.aueb.cf.pharmapp.dto.UserInsertDTO;
import gr.aueb.cf.pharmapp.dto.UserLoginDTO;
import gr.aueb.cf.pharmapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.pharmapp.dto.UserUpdateDTO;
import gr.aueb.cf.pharmapp.exceptions.*;
import gr.aueb.cf.pharmapp.mapper.Mapper;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.security.SecurityUtil;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements IUserService{

    private final IUserDAO userDAO;
    private final IPharmacyDAO pharmacyDAO;

    public UserServiceImpl(EntityManagerFactory emf){
        this.userDAO = new UserDAOImpl(emf);
        this.pharmacyDAO = new PharmacyDAOImpl(emf);
    }

    @Override
    public UserReadOnlyDTO insertUser(UserInsertDTO dto)
            throws UserDAOException, UserAlreadyExistsException {

        try {
            if(userDAO.usernameExists(dto.getUsername())) {
                throw new UserAlreadyExistsException("Username already exists");
            }

            // Hash password before creating User entity
            String hashedPassword = SecurityUtil.hashPassword(dto.getPassword());
            User user = Mapper.mapToUser(dto);
            user.setPassword(hashedPassword);  // Set hashed password

            user = userDAO.save(user);
            return Mapper.mapToReadOnlyDTO(user).orElse(null);
        } catch (UserDAOException e) {
            throw new UserDAOException("Error inserting user"); // Include original exception
        }
    }

    @Override
    public UserReadOnlyDTO updateUser(Long id, UserUpdateDTO dto) throws UserNotFoundException,
            UserAlreadyExistsException, UserDAOException {

        User user;
        User fetchedUser;

        try{
            if(userDAO.getById(id) == null) {
                throw new UserNotFoundException("user with id " + id + " was " +
                        "not found");
            }
            User existingUser = userDAO.getById(id);
            if(userDAO.usernameExists(dto.getUsername())){
                throw new UserAlreadyExistsException("user with username " + dto.getUsername() + " already" +
                        "exists");
            }
            user = Mapper.mapUserUpdateToModel(dto, existingUser );
            User updatedUser = userDAO.update(user);
            return Mapper.mapToReadOnlyDTO(updatedUser).orElse(null);
        } catch(UserDAOException | UserNotFoundException e){
            throw e;
        }
    }

    @Override
    public void deleteUser(Long userIdToDelete, Long loggedInUserId) throws UserNotFoundException, UserAnauthorizedException {
        try {
            User userToDelete = userDAO.getById(userIdToDelete);
            if(userToDelete == null){
                throw new UserNotFoundException("User with id " + userIdToDelete + " was not found");
            }
            userDAO.delete(userIdToDelete, loggedInUserId);
        } catch (UserDAOException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public UserReadOnlyDTO getUserById(Long id) throws UserDAOException, UserNotFoundException {
        try {
            User user = userDAO.getById(id);
            return Mapper.mapToReadOnlyDTO(user)
                    .orElseThrow(()-> new UserNotFoundException("User with id" +
                            " " + id + " was not found"));
        } catch (UserDAOException | UserNotFoundException e) {
            throw e;
        }
    }

    @Override
    public List<UserReadOnlyDTO> getAllUsers() throws UserDAOException {
        try {
            return userDAO.getAll().stream()
                    .map(Mapper::mapToReadOnlyDTO)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (UserDAOException e) {
            throw new RuntimeException("Error retrieving users", e);
        }
    }

    @Override
    public UserReadOnlyDTO getUserByUsername(String username) throws UserNotFoundException, UserDAOException {
        try {
            User user = userDAO.getByUsername(username);
            return Mapper.mapToReadOnlyDTO(user)
                    .orElseThrow(()-> new UserNotFoundException("User with " +
                            "username" + username + " was not found"));
        } catch (UserDAOException | UserNotFoundException e) {
            throw e;
        }
    }

    @Override
    public User getUserEntityByUsername(String username) throws UserNotFoundException, UserDAOException {
        try {
            User user = userDAO.getByUsername(username);
            if(user == null){
                throw new UserNotFoundException("User with username: " + username + " was not found");
            }
            return user;
        } catch (UserDAOException | UserNotFoundException e) {
            throw e;
        }
    }

    @Override
    public boolean authenticate(UserLoginDTO userLoginDTO) throws UserDAOException, UserNotFoundException {

        //Input Validation
        if(userLoginDTO == null || userLoginDTO.getUsername() == null || userLoginDTO.getPassword() == null){
            return false;
        }
        return userDAO.isUserValid(
                userLoginDTO.getUsername(),
                userLoginDTO.getPassword()
        );
    }

    public boolean usernameExists(String username) throws UserDAOException {
        try{
            return userDAO.usernameExists(username);
        } catch (UserDAOException e){
            throw e;
        }
    }

    public boolean emailExists(String email) throws UserDAOException {
        try{
            return userDAO.emailExists(email);
        }catch (UserDAOException e){
            throw e;
        }
    }

    @Override
    public UserReadOnlyDTO assignUserToPharmacy(Long userId, Long pharmacyId, Long adminUserId)
            throws UserNotFoundException, PharmacyNotFoundException, UserAnauthorizedException,
            UserDAOException {
        try {
            // Verify admin status
            User admin = userDAO.getById(adminUserId);
            if (admin == null || admin.getRoleType() != RoleType.ADMIN) {
                throw new UserAnauthorizedException("Only admin can assign users to pharmacies");
            }

            // Get user and pharmacy
            User user = userDAO.getById(userId);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            Pharmacy pharmacy = pharmacyDAO.getById(pharmacyId);
            if (pharmacy == null) {
                throw new PharmacyNotFoundException("Pharmacy not found");
            }

            // Check if pharmacy already has a user
            if (pharmacy.getUser() != null && !pharmacy.getUser().getId().equals(userId)) {
                throw new UserDAOException("Pharmacy already has a different user assigned");
            }

            // Update the relationship
            user.addPharmacy(pharmacy);

            // Save both entities
            userDAO.update(user);
            pharmacyDAO.update(pharmacy);

            return Mapper.mapToReadOnlyDTO(user).orElse(null);
        } catch (PharmacyDAOException e) {
            throw new UserDAOException("Error assigning user to pharmacy: " + e.getMessage());
        }
    }

    @Override
    public UserReadOnlyDTO unassignUserFromPharmacy(Long userId,
                                                    Long pharmacyId,
                                                    Long adminUserId)
            throws UserNotFoundException, UserAnauthorizedException,
            UserDAOException, PharmacyNotFoundException {
        try {
            // Verify admin status
            User admin = userDAO.getById(adminUserId);
            if (admin == null || admin.getRoleType() != RoleType.ADMIN) {
                throw new UserAnauthorizedException("Only admin can unassign users from pharmacies");
            }

            // Get user
            User user = userDAO.getById(userId);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            // Get pharmacy
            Pharmacy pharmacy = pharmacyDAO.getById(pharmacyId);
            if (pharmacy == null) {
                throw new PharmacyNotFoundException("Pharmacy not found");
            }

            // Verify the pharmacy belongs to the user
            if (!user.getPharmacies().contains(pharmacy)) {
                throw new UserAnauthorizedException("This pharmacy doesn't belong to the specified user");
            }

            // Update both sides of the relationship
            user.getPharmacies().remove(pharmacy);
            pharmacy.setUser(null);

            // Save changes
            pharmacyDAO.update(pharmacy);
            User updatedUser = userDAO.update(user);

            return Mapper.mapToReadOnlyDTO(updatedUser).orElse(null);
        } catch (PharmacyDAOException e) {
            throw new UserDAOException("Error unassigning user from pharmacy: " + e.getMessage());
        }
    }

    @Override
    public List<PharmacyContact> getUserContactsWithPharmacies(Long userId) throws UserDAOException {
        return List.of();
    }
}
