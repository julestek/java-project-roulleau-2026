package hr.fesb.java.library.model;

/**
 * Contract for items that support text-based catalogue searching.
 */
public interface Searchable {

    /**
     * Checks whether this item matches a search query.
     * Implementations should be case-insensitive.
     * @param query the search string entered by the user
     * @return true if any searchable field contains the query
     */
    boolean matchesQuery(String query);

    /**
     * Returns a single string concatenating all fields
     * that should be considered during a search.
     * @return concatenated searchable text
     */
    String getSearchableText();
}