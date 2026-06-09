package hr.fesb.java.library;

import java.time.LocalDate;
import java.util.List;

import hr.fesb.java.library.model.AgeRating;
import hr.fesb.java.library.model.AudioFormat;
import hr.fesb.java.library.model.Audiobook;
import hr.fesb.java.library.model.Book;
import hr.fesb.java.library.model.DVD;
import hr.fesb.java.library.model.Library;
import hr.fesb.java.library.model.LibraryItem;
import hr.fesb.java.library.model.Loan;
import hr.fesb.java.library.model.Magazine;
import hr.fesb.java.library.model.Member;


public class LibraryApp {
    public static void main(String[] args) {

        Library library = new Library();
        library.addItem(new Book("B001", "1984", "George Orwell", "9780451524935", 1949, "Dystopia", 1));
        library.addItem(new Book("B002", "Brave New World", "Aldous Huxley", "9780060850524", 1932, "Dystopia", 2));
        library.registerMember(new Member("MEM001", "Alice Martin", "alice@mail.com"));
        library.registerMember(new Member("MEM002", "Bob Smith", "bob@mail.com"));

        // Test 1 -- borrow an item
        try {
            Loan loan = library.borrowItem("MEM001", "B001");
            System.out.println("Test 1 OK -- loan created: " + loan);
        } catch (Exception e) {
            System.out.println("Test 1 FAIL -- " + e.getMessage());
        }

        // Test 2 -- item no longer available after borrow
        System.out.println("Test 2 OK -- B001 available: " + library.findItemById("B001").isAvailable());

        // Test 3 -- borrow unavailable item
        try {
            library.borrowItem("MEM002", "B001");
            System.out.println("Test 3 FAIL -- should have thrown");
        } catch (hr.fesb.java.library.exceptions.ItemNotAvailableException e) {
            System.out.println("Test 3 OK -- ItemNotAvailableException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Test 3 FAIL -- " + e.getMessage());
        }

        // Test 4 -- borrow with unknown member
        try {
            library.borrowItem("MEM999", "B002");
            System.out.println("Test 4 FAIL -- should have thrown");
        } catch (hr.fesb.java.library.exceptions.MemberNotFoundException e) {
            System.out.println("Test 4 OK -- MemberNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Test 4 FAIL -- " + e.getMessage());
        }

        // Test 5 -- return an item
        try {
            Loan loan = library.borrowItem("MEM001", "B002");
            library.returnItem(loan.getLoanId());
            System.out.println("Test 5 OK -- item returned, B002 available: " + library.findItemById("B002").isAvailable());
        } catch (Exception e) {
            System.out.println("Test 5 FAIL -- " + e.getMessage());
        }

        // Test 6 -- active loans count
        System.out.println("Test 6 OK -- active loans: " + library.getActiveLoans().size());

        // Test 7 -- overdue loans
        System.out.println("Test 7 OK -- overdue loans: " + library.getOverdueLoans().size());

        // Test 8 -- loans by member
        try {
            List<Loan> aliceLoans = library.getLoansByMember("MEM001");
            System.out.println("Test 8 OK -- Alice total loans: " + aliceLoans.size());
        } catch (Exception e) {
            System.out.println("Test 8 FAIL -- " + e.getMessage());
        }
    }
}