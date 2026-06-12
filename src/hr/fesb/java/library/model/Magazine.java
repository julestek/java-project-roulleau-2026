package hr.fesb.java.library.model;

/**
 * Represents a magazine issue in the library catalogue.
 */
public class Magazine extends LibraryItem {

    private int    issueNumber;
    private int    month;
    private String publisher;

    /**
     * @param itemId unique identifier
     * @param title magazine title
     * @param issueNumber must be >= 1
     * @param month must be between 1 and 12
     * @param publicationYear must be between 1 and current year
     * @param publisher publisher name (non-empty)
     * @param totalCopies must be >= 1
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Magazine(String itemId, String title, int issueNumber, int month, int publicationYear, String publisher, int totalCopies) {
        super(itemId, title, publicationYear, totalCopies);
        if (issueNumber < 1)
            throw new IllegalArgumentException("Issue number must be >= 1.");
        if (month < 1 || month > 12)
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        if (publisher == null || publisher.trim().isEmpty())
            throw new IllegalArgumentException("Publisher cannot be empty.");
        this.issueNumber = issueNumber;
        this.month = month;
        this.publisher = publisher.trim();
    }

    @Override
    public String getCatalogueEntry() {
        return String.format("MAGAZINE,%s,%s,%d,%d,%d,%s,%d,%d", getItemId(), getTitle(), issueNumber, month, getPublicationYear(), publisher, getTotalCopies(), getCopiesAvailable());
    }

    @Override
    public String getSummary() {
        return String.format("\"%s\" - Issue %d (%02d/%d) by %s", getTitle(), issueNumber, month, getPublicationYear(), publisher);
    }

    @Override
    public boolean matchesQuery(String query) {
        return getSearchableText().toLowerCase().contains(query.toLowerCase().trim());
    }

    @Override
    public String getSearchableText() {
        return getTitle() + " " + publisher + " " + issueNumber;
    }

    @Override
    public String toString() {
        return getSummary() + " | Copies: " + getCopiesAvailable() + "/" + getTotalCopies();
    }

    // --- Getters ---
    public int getIssueNumber() { return issueNumber; }
    public int getMonth() { return month; }
    public String getPublisher() { return publisher; }

    // --- Setters ---
    public void setIssueNumber(int n) {
        if (n < 1)
            throw new IllegalArgumentException("Issue number must be >= 1.");
        this.issueNumber = n;
    }
    public void setMonth(int m) {
        if (m < 1 || m > 12)
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        this.month = m;
    }
    public void setPublisher(String p) {
        if (p == null || p.trim().isEmpty())
            throw new IllegalArgumentException("Publisher cannot be empty.");
        this.publisher = p.trim();
    }
}