package hr.fesb.java.library.model;


import hr.fesb.java.library.exceptions.ItemNotAvailableException;
import java.time.LocalDate;

public abstract class LibraryItem implements Borrowable, Searchable { 

    private String itemId;
    private String title;
    private int publicationYear;
    private int copiesAvailable;
    private int totalCopies;

    /**
    * @param itemId          unique identifier
    * @param title           item title
    * @param publicationYear must be between 1 and the current year
    * @param totalCopies     must be >= 1
    * @throws IllegalArgumentException if any argument is invalid
    */

    public LibraryItem(String itemId, String title, int publicationYear, int totalCopies) {
        if (itemId == null || itemId.trim().isEmpty())
            throw new IllegalArgumentException("Item ID cannot be empty.");
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty.");
        if (publicationYear < 1 || publicationYear > LocalDate.now().getYear())
            throw new IllegalArgumentException(
                "Invalid publication year: " + publicationYear);
        if (totalCopies < 1)
            throw new IllegalArgumentException(
                "Total copies must be at least 1.");

        this.itemId          = itemId.trim();
        this.title           = title.trim();
        this.publicationYear = publicationYear;
        this.totalCopies     = totalCopies;
        this.copiesAvailable = totalCopies;
    }
    

    // ----------------------------------------------------------------
    // Borrowable implementation
    // ----------------------------------------------------------------

    @Override
    public void borrowItem() throws ItemNotAvailableException {
        if (copiesAvailable <= 0)
            throw new ItemNotAvailableException(
                "No copies available for: \"" + title + "\"");
        copiesAvailable--;
    }

    @Override
    public void returnItem() {
        if (copiesAvailable < totalCopies)
            copiesAvailable++;
    }

    @Override
    public boolean isAvailable() {
        return copiesAvailable > 0;
    }

    // ----------------------------------------------------------------
    // Abstract methods — each subclass MUST implement differently
    // ----------------------------------------------------------------

    /**
     * Returns a formatted one-line catalogue entry for this item.
     * Used in the CSV file and in the catalogue table.
     * @return catalogue entry string
     */
    public abstract String getCatalogueEntry();

    /**
     * Returns a short, human-readable summary of this item.
     * Used in dialogs, tooltips, and loan confirmations.
     * @return summary string
     */
    public abstract String getSummary();

    // ----------------------------------------------------------------
    // Getters and setters
    // ----------------------------------------------------------------

    public String getItemId()          { return itemId; }

    public String getTitle()           { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty.");
        this.title = title.trim();
    }

    public int getPublicationYear()              { return publicationYear; }
    public void setPublicationYear(int year) {
        if (year < 1 || year > LocalDate.now().getYear())
            throw new IllegalArgumentException("Invalid publication year: " + year);
        this.publicationYear = year;
    }

    public int getCopiesAvailable()              { return copiesAvailable; }
    public void setCopiesAvailable(int n)        { this.copiesAvailable = n; }

    public int getTotalCopies()                  { return totalCopies; }
    public void setTotalCopies(int n) {
        if (n < 1)
            throw new IllegalArgumentException("Total copies must be >= 1.");
        this.totalCopies = n;
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" (%d) — %d/%d copies available",
            getClass().getSimpleName(), title, publicationYear,
            copiesAvailable, totalCopies);
    }

}