package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class UserNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public UserNotFoundException(String message) {

        super(message);
    }
}
