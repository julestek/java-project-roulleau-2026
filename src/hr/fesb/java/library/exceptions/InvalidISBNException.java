package hr.fesb.java.library.exceptions;

/**
 * Thrown when an ISBN does not meet the 13-digit format requirement.
*/

public class InvalidISBNException extends RuntimeException {
    public InvalidISBNException(String message) {
        super(message);
    }
}