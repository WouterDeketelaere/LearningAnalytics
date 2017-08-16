package be.kul.mai.la.services.exceptions;

/**
 * Created by wouter on 25.12.16.
 */
public class LAException extends RuntimeException {
    public LAException(String message) {
        super(message);
    }

    public LAException(String message, Throwable cause) {
        super(message, cause);
    }

    public LAException(Throwable cause) {
        super(cause);
    }
}
