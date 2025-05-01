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
    Pharmacy getPharmacyWithRelationsById(Long id) throws PharmacyDAOException;
    Pharmacy getByName(String name) throws PharmacyDAOException;
    List<Pharmacy> searchPharmaciesByName(String name) throws PharmacyDAOException;
    List<Pharmacy> searchPharmaciesByUser(String username) throws PharmacyDAOException;
    public boolean existsByName(String name) throws PharmacyDAOException;

}
