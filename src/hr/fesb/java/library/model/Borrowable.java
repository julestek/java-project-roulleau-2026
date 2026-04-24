package hr.fesb.java.library.model;

import hr.fesb.java.library.exceptions.ItemNotAvailableException;

public interface Borrowable {

    void borrowItem() throws ItemNotAvailableException;

    void returnItem();

    boolean isAvailable();

}