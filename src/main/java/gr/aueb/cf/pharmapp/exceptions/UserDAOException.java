package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class UserDAOException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserDAOException(String s) {
        super(s);
    }
}
