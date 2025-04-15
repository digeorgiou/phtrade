package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class PharmacyAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public PharmacyAlreadyExistsException(String message) {
        super(message);
    }
}
