package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.dao.*;
import gr.aueb.cf.pharmapp.dto.BasePharmacyContactDTO;
import gr.aueb.cf.pharmapp.exceptions.*;
import gr.aueb.cf.pharmapp.mapper.Mapper;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PharmacyContactServiceImpl implements IPharmacyContactService {
    private final IPharmacyContactDAO contactDAO;
    private final IUserDAO userDAO;
    private final IPharmacyDAO pharmacyDAO;

    public PharmacyContactServiceImpl(EntityManagerFactory emf) {
        this.contactDAO = new PharmacyContactDAOImpl(emf);
        this.userDAO = new UserDAOImpl(emf);
        this.pharmacyDAO = new PharmacyDAOImpl(emf);
    }

    @Override
    public BasePharmacyContactDTO saveContact(BasePharmacyContactDTO contactDTO)
            throws PharmacyContactAlreadyExistsException,
            PharmacyContactDAOException,
            PharmacyDAOException, UserNotFoundException, PharmacyNotFoundException {
        try {
            // Validate user exists
            User user = userDAO.getById(contactDTO.getUserId());
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            // Validate pharmacy exists
            Pharmacy pharmacy = pharmacyDAO.getById(contactDTO.getPharmacyId());
            if (pharmacy == null) {
                throw new PharmacyNotFoundException("Pharmacy not found");
            }

            // Check if contact already exists
            if (contactDAO.existsByUserAndPharmacy(contactDTO.getUserId(), contactDTO.getPharmacyId())) {
                throw new PharmacyContactAlreadyExistsException("This pharmacy is already in your " +
                        "contacts");
            }

            // Create new contact
            PharmacyContact contact = new PharmacyContact();
            contact.setUser(user);
            contact.setPharmacy(pharmacy);
            contact.setContactName(contactDTO.getContactName());

            // Save and return DTO
            PharmacyContact savedContact = contactDAO.save(contact);
            BasePharmacyContactDTO dto =
                    Mapper.mapContactToDTO(savedContact).orElse(null);
            return dto;
        } catch (PharmacyContactDAOException | UserDAOException | PharmacyDAOException e) {
            throw new PharmacyContactDAOException("Error saving contact: " + e.getMessage());
        }
    }

    @Override
    public void deleteContact(Long contactId) throws PharmacyContactNotFoundException, PharmacyContactDAOException {
        try {
            if (contactDAO.findById(contactId) == null) {
                throw new PharmacyContactNotFoundException("Contact not found");
            }
            contactDAO.delete(contactId);
        } catch (PharmacyContactDAOException e) {
            throw new PharmacyContactDAOException("Error deleting contact: ");
        }
    }

    @Override
    public BasePharmacyContactDTO findById(Long contactId) throws PharmacyContactNotFoundException, PharmacyContactDAOException{
        PharmacyContact contact;

        try{
            contact = contactDAO.findById(contactId);
            return Mapper.mapContactToDTO(contact)
                    .orElseThrow(()->new PharmacyContactNotFoundException(
                            "Contact with id " + contactId + " was not found."));

        }catch (PharmacyContactDAOException e){
            throw e;
        }
    }

    @Override
    public List<BasePharmacyContactDTO> getContactsForUser(Long userId) throws PharmacyContactDAOException {
        try {
            List<PharmacyContact> contacts = contactDAO.findByUser(userId);
            return contacts.stream()
                    .map(Mapper::mapContactToDTO)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (PharmacyContactDAOException e) {
            throw e;
        }
    }

    @Override
    public boolean contactExists(Long userId, Long pharmacyId) throws PharmacyContactDAOException {
        try {
            return contactDAO.existsByUserAndPharmacy(userId, pharmacyId);
        } catch (PharmacyContactDAOException e) {
            throw  e;
        }
    }

}
