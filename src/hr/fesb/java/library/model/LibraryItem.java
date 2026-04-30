package hr.fesb.java.library.model;


import hr.fesb.java.library.exceptions.ItemNotAvailableException;
import java.time.LocalDate;

public abstract class LibraryItem implements Borrowable, Searchable { 

    private String itemId;
    private String title;
    private int publicationYear;
    private int copiesAvailable;
    private int totalCopies;

}