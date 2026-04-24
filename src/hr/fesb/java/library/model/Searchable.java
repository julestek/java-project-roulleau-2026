package hr.fesb.java.library.model;

public interface Searchable {

    boolean matchesQuery(String query);

    String getSearchableText();

}