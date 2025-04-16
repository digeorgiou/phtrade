package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyContactDAOException;
import gr.aueb.cf.pharmapp.model.PharmacyContact;

import java.util.List;

public interface IPharmacyContactDAO {
    PharmacyContact save(PharmacyContact contact) throws PharmacyContactDAOException;
    void delete(Long contactId) throws PharmacyContactDAOException;
    PharmacyContact findById(Long id) throws PharmacyContactDAOException;
    List<PharmacyContact> findByUser(Long userId) throws PharmacyContactDAOException;
    boolean existsByUserAndPharmacy(Long userId, Long pharmacyId) throws PharmacyContactDAOException;
}
