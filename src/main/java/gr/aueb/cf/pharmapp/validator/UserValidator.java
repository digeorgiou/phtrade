package gr.aueb.cf.pharmapp.validator;

import gr.aueb.cf.pharmapp.dao.IUserDAO;
import gr.aueb.cf.pharmapp.dao.UserDAOImpl;
import gr.aueb.cf.pharmapp.dto.BaseUserDTO;
import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.service.IUserService;
import gr.aueb.cf.pharmapp.service.UserServiceImpl;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

public class UserValidator<T> {

        private final EntityManagerFactory emf;
        private final IUserDAO userDAO;
        private final IUserService userService;

        public UserValidator(EntityManagerFactory emf) {
            this.emf = emf;
            this.userDAO = new UserDAOImpl(emf);
            this.userService = new UserServiceImpl(emf);
        }

        public <T extends  BaseUserDTO> Map<String, String > validate(T dto)
                throws UserDAOException {
            Map<String, String> errors = new HashMap<>();

            if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
                errors.put("confirmPassword", "Το password και το confirmedPassword δεν είναι ίδια.");
            }

            if (dto.getPassword().length() < 5 || dto.getPassword().length() > 32 ) {
                errors.put("password", "Το password πρέπει να είναι μεταξύ 5 και 32");
            }

            if (dto.getUsername().matches("^.*\\s+.*$")) {
                errors.put("username", "Το username δεν πρέπει να περιλαμβάνει κενά");
            }

            if (dto.getPassword().matches("^.*\\s+.*$")) {
                errors.put("password", "Το password δεν πρέπει να περιλαμβάνει κενά");
            }

            if (!dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                errors.put("email", "Το email δεν είναι έγκυρο");
            }


            if(userService.usernameExists(dto.getUsername())){
                errors.put("username", "Το username χρησιμοποιείται");
            }

            if(userService.emailExists(dto.getEmail())){
                errors.put("email", "Το email χρησιμοποιείται");
            }
            return errors;
        }
}
