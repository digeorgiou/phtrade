package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.PharmacyDAOException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.User;

import java.util.List;

public interface IPharmacyDAO {

    //Basic Services
    Pharmacy save(Pharmacy pharmacy) throws PharmacyDAOException;
    Pharmacy update(Pharmacy pharmacy) throws PharmacyDAOException;
    void delete(Long id) throws PharmacyDAOException;
    Pharmacy getById(Long id) throws PharmacyDAOException;
    List<Pharmacy> getAll() throws PharmacyDAOException;

    //Queries
    List<Pharmacy> getByName(String name) throws PharmacyDAOException;
    Pharmacy findByUser(User user) throws PharmacyDAOException;
    void linkUserToPharmacy(User user, Pharmacy pharmacy) throws PharmacyDAOException;
    public boolean existsByName(String name) throws PharmacyDAOException;

}
