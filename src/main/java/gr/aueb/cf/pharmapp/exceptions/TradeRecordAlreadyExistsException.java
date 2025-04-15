package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class TradeRecordAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public TradeRecordAlreadyExistsException(String message) {

        super(message);
    }
}
