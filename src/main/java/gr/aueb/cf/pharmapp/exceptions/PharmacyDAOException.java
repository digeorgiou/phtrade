package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class PharmacyDAOException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public PharmacyDAOException(String message) {

        super(message);
    }
}
