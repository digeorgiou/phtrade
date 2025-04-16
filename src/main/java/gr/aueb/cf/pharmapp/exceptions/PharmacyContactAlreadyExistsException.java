package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class PharmacyContactAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public PharmacyContactAlreadyExistsException(String message) {

        super(message);
    }
}
