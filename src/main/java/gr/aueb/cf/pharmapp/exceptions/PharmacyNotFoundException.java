package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class PharmacyNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public PharmacyNotFoundException(String message) {

        super(message);
    }
}
