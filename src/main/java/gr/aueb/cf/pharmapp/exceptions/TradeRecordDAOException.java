package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class TradeRecordDAOException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;
    public TradeRecordDAOException(String message) {

      super(message);
    }
}
