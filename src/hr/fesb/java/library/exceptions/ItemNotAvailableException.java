package hr.fesb.java.library.exceptions;

/** Thrown when a borrow is attempted on an item with no copies available. */
public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
