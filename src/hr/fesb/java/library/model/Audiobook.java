package hr.fesb.java.library.model;

/**
 * Represents an audiobook in the library catalogue.
*/
public class Audiobook extends LibraryItem{

    private String narrator;
    private double durationHours;
    private AudioFormat format;

    /**
     * @param itemId unique identifier
     * @param title audiobook title
     * @param narrator narrator name (non-empty)
     * @param durationHours must be > 0
     * @param publicationYear must be between 1 and current year
     * @param format MP3 or CD
     * @param totalCopies must be >= 1
     * @throws IllegalArgumentException if any argument is invalid
    */

    public Audiobook(String itemId, String title, String narrator, double durationHours, int publicationYear, AudioFormat format, int totalCopies) {
        super(itemId, title, publicationYear, totalCopies);
        if (narrator == null || narrator.trim().isEmpty())
            throw new IllegalArgumentException("Narrator cannot be empty.");
        if (durationHours <= 0)
            throw new IllegalArgumentException("Duration must be > 0 hours.");
        this.narrator = narrator.trim();
        this.durationHours = durationHours;
        this.format = (format != null) ? format : AudioFormat.MP3;
    }

    @Override
    public String getCatalogueEntry() {
        return String.format("AUDIOBOOK,%s,%s,%s,%.1f,%d,%s,%d,%d", getItemId(), getTitle(), narrator, durationHours, getPublicationYear(), format, getTotalCopies(), getCopiesAvailable());
    }

    @Override
    public String getSummary() {
        return String.format("\"%s\" narrated by %s, %.1f h (%s)", getTitle(), narrator, durationHours, format);
    }

    @Override
    public boolean matchesQuery(String query) {
        return getSearchableText().toLowerCase().contains(query.toLowerCase().trim());
    }

    @Override
    public String getSearchableText() {
        return getTitle() + " " + narrator + " " + format;
    }

    @Override
    public String toString() {
        return getSummary() + " | Copies: " + getCopiesAvailable() + "/" + getTotalCopies();
    }

    // --- Getters ---
    public String getNarrator(){ return narrator; }
    public double getDurationHours(){ return durationHours; }
    public AudioFormat getFormat(){ return format; }

    // --- Setters ---
    public void setNarrator(String narrator) {
        if (narrator == null || narrator.trim().isEmpty())
            throw new IllegalArgumentException("Narrator cannot be empty.");
        this.narrator = narrator.trim();
    }
    public void setDurationHours(double d) {
        if (d <= 0)
            throw new IllegalArgumentException("Duration must be > 0 hours.");
        this.durationHours = d;
    }
    public void setFormat(AudioFormat format) {
        this.format = format;
    }

}
