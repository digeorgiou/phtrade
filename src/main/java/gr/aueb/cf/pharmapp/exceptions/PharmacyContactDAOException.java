package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class PharmacyContactDAOException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;
    public PharmacyContactDAOException(String message) {

      super(message);
    }
}
