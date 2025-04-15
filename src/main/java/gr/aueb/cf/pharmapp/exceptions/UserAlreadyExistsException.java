package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class UserAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
