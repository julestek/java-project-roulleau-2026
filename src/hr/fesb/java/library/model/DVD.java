package hr.fesb.java.library.model;

public class DVD extends LibraryItem {
        
    private String director;
    private int durationMinutes;
    private AgeRating ageRating;

    /**
     * @param itemId unique identifier
     * @param title DVD title
     * @param director director name (non-empty)
     * @param durationMinutes must be > 0
     * @param publicationYear must be between 1 and current year
     * @param ageRating age rating classification
     * @param totalCopies must be > 0
     * @throws IllegalArgumentException if any argument is invalid
    */

    public DVD(String itemId, String title, String director, int durationMinutes, int publicationYear, AgeRating ageRating, int totalCopies){
        super(itemId, title, publicationYear, totalCopies);
        if (director == null || director.trim().isEmpty())
            throw new IllegalArgumentException("Director field cannot be empty");
        if (durationMinutes < 1)
            throw new IllegalArgumentException("Duration must be > than 0");
        this.director        = director.trim();
        this.durationMinutes = durationMinutes;
        this.ageRating       = (ageRating != null) ? ageRating : AgeRating.G;
    }

    // Overriding methods from LibraryItem

    @Override
    public String getCatalogueEntry(){
        return String.format("DVD,%s,%s,%s,%d,%d,%s,%d,%d", getItemId(), getTitle(), director, durationMinutes, getPublicationYear(), ageRating, getTotalCopies(), getCopiesAvailable());
    }

    @Override
    public String getSummary() {
        return String.format("\"%s\" dir . %s (%d) - %d min, rated %s", getTitle(), director, getPublicationYear(), durationMinutes, ageRating);
    }

    @Override
    public boolean matchesQuery(String query){
        return getSearchableText().toLowerCase().contains(query.toLowerCase().trim());
    }

    @Override
    public String getSearchableText() {
        return getTitle() + " " + director + " " + ageRating;
    }

    @Override
    public String toString() {
        return getSummary() + " | Copies: " + getCopiesAvailable() + "/" + getTotalCopies();
    }


    // --- Getters ---
    public String getDirector() { return director; }
    public int getDurationMinutes() { return durationMinutes; }
    public AgeRating getAgeRating() { return ageRating; }

    // --- Setters ---
    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty())
            throw new IllegalArgumentException("Director cannot be empty.");
        this.director = director.trim();
    }
    public void setDurationMinutes(int d) {
        if (d < 1)
            throw new IllegalArgumentException("Duration must be > 0 minute.");
        this.durationMinutes = d;
    }
    public void setAgeRating(AgeRating ageRating) {
        this.ageRating = ageRating;
    }

}
