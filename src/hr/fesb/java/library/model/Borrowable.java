package hr.fesb.java.library.model;

import hr.fesb.java.library.exceptions.ItemNotAvailableException;

/**
 * Contract for library items that can be lent to members.
 */
public interface Borrowable {

    /**
     * Marks one copy of this item as borrowed.
     * @throws ItemNotAvailableException if no copy is currently available
     */
    void borrowItem() throws ItemNotAvailableException;

    /**
     * Marks one copy as returned, incrementing availability.
     */
    void returnItem();

    /**
     * @return true if at least one copy is available for borrowing
     */
    boolean isAvailable();
}