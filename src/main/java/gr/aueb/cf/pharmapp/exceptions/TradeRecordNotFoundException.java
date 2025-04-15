package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class TradeRecordNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public TradeRecordNotFoundException(String message) {

        super(message);
    }
}
