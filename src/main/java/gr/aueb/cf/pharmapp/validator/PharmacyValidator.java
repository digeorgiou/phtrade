package gr.aueb.cf.pharmapp.validator;

import gr.aueb.cf.pharmapp.dao.IPharmacyDAO;
import gr.aueb.cf.pharmapp.dao.PharmacyDAOImpl;
import gr.aueb.cf.pharmapp.dto.BasePharmacyDTO;
import gr.aueb.cf.pharmapp.exceptions.PharmacyDAOException;
import gr.aueb.cf.pharmapp.service.IPharmacyService;
import gr.aueb.cf.pharmapp.service.PharmacyServiceImpl;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

public class PharmacyValidator {

    private final EntityManagerFactory emf;
    private final IPharmacyDAO pharmacyDAO;
    private final IPharmacyService pharmacyService;

    public PharmacyValidator(EntityManagerFactory emf){
        this.emf = emf;
        this.pharmacyDAO = new PharmacyDAOImpl(emf);
        this.pharmacyService = new PharmacyServiceImpl(emf);
    }

    public <T extends BasePharmacyDTO> Map<String, String > validate(T dto)
            throws PharmacyDAOException{
        Map<String, String> errors = new HashMap<>();

        if(dto.getName().length() < 10 || dto.getName().length() > 50){
            errors.put("name", "Το όνομα πρέπει να ειναι μεταξύ 10 και 50 " +
                    "χαρακτήρες");
        }

        if (dto.getName().matches("^.*\\s+.*$")) {
            errors.put("name", "Το όνομα δεν πρέπει να περιλαμβάνει κενά");
        }

        if (pharmacyService.nameExists(dto.getName())){
            errors.put("name", "Το όνομα " + dto.getName() + " " +
                    "χρησιμοποιείται ήδη");
        }
        return  errors;
    }
}
