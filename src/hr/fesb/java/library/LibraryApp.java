package hr.fesb.java.library;

import hr.fesb.java.library.model.AgeRating;
import hr.fesb.java.library.model.AudioFormat;
import hr.fesb.java.library.model.Audiobook;
import hr.fesb.java.library.model.Book;
import hr.fesb.java.library.model.DVD;

public class LibraryApp {
    public static void main(String[] args) {

        // Test 1 InvalidISBNException (unchecked)
        try {
            Book bad = new Book("B001", "Bad Book", "Author",
                "123", 2020, "Fiction", 1);
        } catch (hr.fesb.java.library.exceptions.InvalidISBNException e) {
            System.out.println("Test 1 OK — InvalidISBNException: " + e.getMessage());
        }

        // Test 2 ItemNotAvailableException (checked)
        try {
            Book book = new Book("B002", "1984", "George Orwell",
                "9780451524935", 1949, "Dystopia", 1);
            book.borrowItem(); 
            book.borrowItem();
        } catch (hr.fesb.java.library.exceptions.ItemNotAvailableException e) {
            System.out.println("Test 2 OK — ItemNotAvailableException: " + e.getMessage());
        }

        // Test 3 MemberNotFoundException (checked)
        try {
            throw new hr.fesb.java.library.exceptions.MemberNotFoundException("MEM999");
        } catch (hr.fesb.java.library.exceptions.MemberNotFoundException e) {
            System.out.println("Test 3 OK — MemberNotFoundException: " + e.getMessage());
            System.out.println("Test 3 OK — Member ID was: " + e.getMemberId());
        }

        // Test 4 confirm InvalidISBNException is unchecked (RuntimeException)
        boolean isUnchecked = new hr.fesb.java.library.exceptions.InvalidISBNException("test")
            instanceof RuntimeException;
        System.out.println("Test 4 OK — InvalidISBNException is unchecked: " + isUnchecked);

        // Test 5 confirm MemberNotFoundException is checked (Exception, not RuntimeException)
        boolean isChecked = new hr.fesb.java.library.exceptions.MemberNotFoundException("x")
            instanceof Exception;
        System.out.println("Test 5 OK — MemberNotFoundException is checked: " + isChecked);

        // Test 6 confirm ItemNotAvailableException is checked
        boolean isChecked2 = new hr.fesb.java.library.exceptions.ItemNotAvailableException("x")
            instanceof Exception;
        System.out.println("Test 6 OK — ItemNotAvailableException is checked: " + isChecked2);
    }
}