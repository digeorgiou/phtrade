package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class UserAnauthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public UserAnauthorizedException(String message) {
        super(message);
    }
}
