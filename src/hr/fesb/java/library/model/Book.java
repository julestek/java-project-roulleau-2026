package hr.fesb.java.library.model;

import hr.fesb.java.library.exceptions.InvalidISBNException;

/**
 * Represents a physical book in the library catalogue.
 */
public class Book extends LibraryItem {

    private String author;
    private String isbn;
    private String genre;

    /**
     * @param itemId          unique identifier
     * @param title           book title
     * @param author          author name (non-empty)
     * @param isbn            must be exactly 13 digits
     * @param publicationYear must be between 1 and current year
     * @param genre           book genre
     * @param totalCopies     must be >= 1
     * @throws InvalidISBNException     if ISBN is not 13 digits
     * @throws IllegalArgumentException if any other argument is invalid
     */

    public Book(String itemId, String title, String author, String isbn, int publicationYear, String genre, int totalCopies) {
        super(itemId, title, publicationYear, totalCopies);
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Author cannot be empty.");
        if (!isbn.matches("\\d{13}"))
            throw new InvalidISBNException(
                "ISBN must be exactly 13 digits. Received: " + isbn);
        this.author = author.trim();
        this.isbn   = isbn;
        this.genre  = (genre != null) ? genre.trim() : "Unknown";
    }

    @Override
    public String getCatalogueEntry() {
        return String.format("BOOK,%s,%s,%s,%s,%d,%s,%d,%d", getItemId(), getTitle(), author, isbn, getPublicationYear(), genre, getTotalCopies(), getCopiesAvailable());
    }

    @Override
    public String getSummary() {
        return String.format("\"%s\" by %s — %s (%d)", getTitle(), author, genre, getPublicationYear());
    }

    @Override
    public boolean matchesQuery(String query) {
        return getSearchableText().toLowerCase().contains(query.toLowerCase().trim());
    }

    @Override
    public String getSearchableText() {
        return getTitle() + " " + author + " " + isbn + " " + genre;
    }

    @Override
    public String toString() {
        return getSummary() + " | ISBN: " + isbn
            + " | Copies: " + getCopiesAvailable()
            + "/" + getTotalCopies();
    }

    // --- Getters ---
    public String getAuthor() { return author; }
    public String getIsbn()   { return isbn; }
    public String getGenre()  { return genre; }

    // --- Setters ---
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Author cannot be empty.");
        this.author = author.trim();
    }
    public void setGenre(String genre) {
        this.genre = (genre != null) ? genre.trim() : "Unknown";
    }
}