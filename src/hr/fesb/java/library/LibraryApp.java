package hr.fesb.java.library;

import hr.fesb.java.library.io.LibraryFileManager;
import hr.fesb.java.library.model.*;

/**
 * This is where the App will be loaded each time.
*/
public class LibraryApp {

    public static void main(String[] args) {

        Library library = new Library();
        LibraryFileManager fm = new LibraryFileManager();

        // Save on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fm.saveAll(library);
        }));

        // Test data
        library.addItem(new Book("B001", "1984", "George Orwell", "9780451524935", 1949, "Dystopia", 2));
        library.addItem(new Book("B002", "Brave New World", "Aldous Huxley", "9780060850524", 1932, "Dystopia", 1));
        library.addItem(new DVD("D001", "Inception", "Christopher Nolan", 148, 2010, AgeRating.PG13, 2));
        library.addItem(new Magazine("M001", "National Geographic", 312, 4, 2024, "National Geographic Society", 2));
        library.addItem(new Audiobook("A001", "Dune", "Scott Brick", 21.5, 1965, AudioFormat.MP3, 1));
        library.registerMember(new Member("MEM001", "Alice Martin", "alice@mail.com"));
        library.registerMember(new Member("MEM002", "Bob Smith", "bob@mail.com"));

        try {
            Loan loan = library.borrowItem("MEM001", "B001");
            System.out.println("Loan created: " + loan);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Running -- press Ctrl+C to save and exit.");

        // Keep app running so shutdown hook triggers on Ctrl+C
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}