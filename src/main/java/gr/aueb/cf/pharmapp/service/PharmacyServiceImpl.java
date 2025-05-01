package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.dao.IPharmacyDAO;
import gr.aueb.cf.pharmapp.dao.IUserDAO;
import gr.aueb.cf.pharmapp.dao.PharmacyDAOImpl;
import gr.aueb.cf.pharmapp.dao.UserDAOImpl;
import gr.aueb.cf.pharmapp.dto.PharmacyInsertDTO;
import gr.aueb.cf.pharmapp.dto.PharmacyReadOnlyDTO;
import gr.aueb.cf.pharmapp.dto.PharmacyUpdateDTO;
import gr.aueb.cf.pharmapp.exceptions.*;
import gr.aueb.cf.pharmapp.mapper.Mapper;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.User;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PharmacyServiceImpl implements IPharmacyService{

    private final IPharmacyDAO pharmacyDAO;
    private final IUserDAO userDAO;

    public PharmacyServiceImpl(EntityManagerFactory emf) {
        this.pharmacyDAO = new PharmacyDAOImpl(emf);
        this.userDAO = new UserDAOImpl(emf);
    }


    @Override
    public PharmacyReadOnlyDTO createPharmacy(PharmacyInsertDTO dto, Long creatorUserId)
            throws PharmacyAlreadyExistsException, PharmacyDAOException,
            UserNotFoundException, UserDAOException {
        Pharmacy pharmacy;
        Pharmacy insertedPharmacy;

        try{
            User creator = userDAO.getById(creatorUserId);
            if (creator == null) {
                throw new UserNotFoundException("Creator user not found");
            }

            if (pharmacyDAO.existsByName(dto.getName())){
                throw new PharmacyAlreadyExistsException("Pharmacy with name " + dto.getName() + " already exists");
            }

            pharmacy = Mapper.mapPharmacyInsertToModel(dto);

            // Link to creator if not admin
            if (creator.getRoleType() != RoleType.ADMIN) {
                pharmacy.setUser(creator);
            }

            insertedPharmacy = pharmacyDAO.save(pharmacy);

            return Mapper.mapPharmacyToReadOnlyDTO(insertedPharmacy).orElse(null);
        } catch (PharmacyDAOException | PharmacyAlreadyExistsException |
                 UserDAOException e){
            //rollback
            throw e;
        }
    }

    @Override
    public PharmacyReadOnlyDTO updatePharmacy(Long id, PharmacyUpdateDTO dto, Long updaterUserId
                                              ) throws PharmacyDAOException,
            PharmacyAlreadyExistsException, PharmacyNotFoundException,
            UserAnauthorizedException, UserNotFoundException, UserDAOException {

        try{
            Pharmacy existing = pharmacyDAO.getById(id);
            if(existing == null){
                throw new PharmacyNotFoundException("Pharmacy with id " + id + " was not found");
            }

            // Check if updater is authorized (admin or creator)
            User updater = userDAO.getById(updaterUserId);
            if (updater == null) {
                throw new UserNotFoundException("Updater user not found");
            }
            if (updater.getRoleType() != RoleType.ADMIN &&
                    (existing.getUser() == null || !existing.getUser().getId().equals(updaterUserId))) {
                throw new UserAnauthorizedException("Only admin or pharmacy creator can update " +
                        "this pharmacy");
            }

            if (!existing.getName().equals(dto.getName())) {
                if (pharmacyDAO.getByName(dto.getName() ) != null ){
                    throw new PharmacyAlreadyExistsException(
                            "Pharmacy with name '" + dto.getName() + "' already exists");
                }
            }


            Pharmacy updated = Mapper.mapPharmacyUpdateToModel(dto, existing);
            updated = pharmacyDAO.update(updated);


            return Mapper.mapPharmacyToReadOnlyDTO(updated).orElse(null);
        } catch (PharmacyDAOException | PharmacyAlreadyExistsException | UserDAOException e){
            //rollback
            throw e;
        }
    }

    @Override
    public void deletePharmacy(Long id, Long deleterUserId) throws PharmacyNotFoundException,
            PharmacyDAOException, UserNotFoundException, UserDAOException {
        try{
            Pharmacy pharmacy = pharmacyDAO.getById(id);
            if(pharmacy == null){
                throw new PharmacyNotFoundException("Pharmacy was not found");
            }

            // Check if deleter is authorized (admin or creator)
            User deleter = userDAO.getById(deleterUserId);
            if (deleter == null) {
                throw new UserNotFoundException("Deleter user not found");
            }

            if (deleter.getRoleType() != RoleType.ADMIN &&
                    (pharmacy.getUser() == null || !pharmacy.getUser().getId().equals(deleterUserId))) {
                throw new UserAnauthorizedException("Only admin or pharmacy creator can delete" +
                        "this pharmacy");
            }

            pharmacyDAO.delete(id);
        } catch ( PharmacyDAOException | PharmacyNotFoundException | UserDAOException e) {
            throw e;
        }
    }

    @Override
    public PharmacyReadOnlyDTO getPharmacyById(Long id) throws PharmacyNotFoundException, PharmacyDAOException {
        Pharmacy pharmacy;

        try{
            pharmacy = pharmacyDAO.getById(id);
            return Mapper.mapPharmacyToReadOnlyDTO(pharmacy)
                    .orElseThrow(()->new PharmacyNotFoundException("Pharmacy with id " + id + " was not found."));

        }catch (PharmacyDAOException | PharmacyNotFoundException e){
            throw e;
        }
    }

    @Override
    public Pharmacy getPharmacyEntityById(Long id) throws PharmacyNotFoundException, PharmacyDAOException {
        try {
            Pharmacy pharmacy = pharmacyDAO.getPharmacyWithRelationsById(id);
            if (pharmacy == null) {
                throw new PharmacyNotFoundException("Pharmacy with id " + id + " was not found");
            }

            // Initialize necessary collections
            Hibernate.initialize(pharmacy.getUser());
            Hibernate.initialize(pharmacy.getContactReferences());

            return pharmacy;
        } catch (PharmacyDAOException e) {
            throw e;
        }
    }

    @Override
    public List<PharmacyReadOnlyDTO> getAllPharmacies() throws PharmacyDAOException {
        List<Pharmacy> pharmacies;

        try{
            pharmacies = pharmacyDAO.getAll();
            return pharmacies.stream()
                    .map(Mapper::mapPharmacyToReadOnlyDTO)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        }catch (PharmacyDAOException e){
            throw new PharmacyDAOException("Error retrieving all pharmacies");
        }
    }

    @Override
    public PharmacyReadOnlyDTO getPharmaciesByName(String name) throws PharmacyDAOException {
        Pharmacy pharmacy;

        try{
            pharmacy = pharmacyDAO.getByName(name);
            return  Mapper.mapPharmacyToReadOnlyDTO(pharmacy).orElse(null);
        }catch (PharmacyDAOException e){
            throw new PharmacyDAOException("Error retrieving pharmacies by " +
                    "name " + name);
        }
    }

    @Override
    public List<PharmacyReadOnlyDTO> searchPharmaciesByName(String name) throws PharmacyDAOException {
        try {
            List<Pharmacy> pharmacies = pharmacyDAO.searchPharmaciesByName(name);
            return pharmacies.stream()
                    .map(Mapper::mapPharmacyToReadOnlyDTO)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        }catch (PharmacyDAOException e){
            throw new PharmacyDAOException("Error retrieving pharmacies by " +
                    "name " + name);
        }
    }

    @Override
    public List<PharmacyReadOnlyDTO> searchPharmaciesByUser(String username) throws PharmacyDAOException {
        try {
            List<Pharmacy> pharmacies = pharmacyDAO.searchPharmaciesByUser(username);
            return pharmacies.stream()
                    .map(Mapper::mapPharmacyToReadOnlyDTO)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (PharmacyDAOException e){
            throw new PharmacyDAOException("Error retrieving pharmacies by " +
                    "user " + username);
        }
    }


    @Override
    public boolean nameExists(String name) throws PharmacyDAOException {
        try{
            return pharmacyDAO.existsByName(name);
        } catch (PharmacyDAOException e) {
            throw e;
        }
    }


    @Override
    public boolean canAddAsContact(Long userId, Long pharmacyId) throws PharmacyNotFoundException, PharmacyDAOException, UserDAOException {
        return false;
    }
}
