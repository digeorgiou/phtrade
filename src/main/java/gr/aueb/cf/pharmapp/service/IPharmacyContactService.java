package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.dto.BasePharmacyContactDTO;
import gr.aueb.cf.pharmapp.exceptions.*;

import java.util.List;

public interface IPharmacyContactService {

        BasePharmacyContactDTO saveContact(BasePharmacyContactDTO contactDTO) throws PharmacyContactAlreadyExistsException,
                PharmacyContactDAOException, PharmacyDAOException
                , UserNotFoundException, PharmacyNotFoundException;
        void deleteContact(Long contactId) throws PharmacyContactNotFoundException, PharmacyContactDAOException;
        BasePharmacyContactDTO findById(Long contactId) throws PharmacyContactNotFoundException, PharmacyContactDAOException;
        List<BasePharmacyContactDTO> getContactsForUser(Long userId) throws PharmacyContactDAOException;
        boolean contactExists(Long userId, Long pharmacyId) throws PharmacyContactDAOException;

}
