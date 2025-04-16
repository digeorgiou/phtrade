package gr.aueb.cf.pharmapp.exceptions;

import java.io.Serial;

public class PharmacyContactNotFoundException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;
    public PharmacyContactNotFoundException(String message) {
        super(message);
    }
}
