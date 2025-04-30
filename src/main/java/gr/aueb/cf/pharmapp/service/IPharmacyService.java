package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.dto.PharmacyInsertDTO;
import gr.aueb.cf.pharmapp.dto.PharmacyReadOnlyDTO;
import gr.aueb.cf.pharmapp.dto.PharmacyUpdateDTO;
import gr.aueb.cf.pharmapp.exceptions.*;

import java.util.List;

public interface IPharmacyService {

    PharmacyReadOnlyDTO createPharmacy(PharmacyInsertDTO dto,
                                       Long creatorUserId) throws PharmacyAlreadyExistsException,
            PharmacyDAOException, UserNotFoundException, UserDAOException;
    PharmacyReadOnlyDTO updatePharmacy(Long id, PharmacyUpdateDTO dto,
                                       Long updaterUserId) throws PharmacyDAOException, PharmacyAlreadyExistsException,
            PharmacyNotFoundException, UserAnauthorizedException,
            UserNotFoundException, UserDAOException;
    void deletePharmacy(Long id, Long deleterUserId) throws PharmacyNotFoundException,
            PharmacyDAOException, UserNotFoundException, UserDAOException;
    boolean nameExists(String name) throws PharmacyDAOException;
    PharmacyReadOnlyDTO getPharmacyById(Long id) throws PharmacyNotFoundException, PharmacyDAOException;
    List<PharmacyReadOnlyDTO> searchPharmaciesByName(String name) throws PharmacyDAOException;
    List<PharmacyReadOnlyDTO> getAllPharmacies() throws PharmacyDAOException;
    PharmacyReadOnlyDTO getPharmaciesByName(String name) throws PharmacyDAOException;
    boolean canAddAsContact(Long userId, Long pharmacyId)
            throws PharmacyNotFoundException, PharmacyDAOException, UserDAOException;

}
