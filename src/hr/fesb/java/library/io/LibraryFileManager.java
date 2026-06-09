package hr.fesb.java.library.io;

import hr.fesb.java.library.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Handles all CSV file I/O for the library system.
 * Saves and loads items, members and loans.
*/
public class LibraryFileManager {

    private static final String DATA_DIR = "data/";
    private static final String ITEMS_FILE = DATA_DIR + "items.csv";
    private static final String MEMBERS_FILE = DATA_DIR + "members.csv";
    private static final String LOANS_FILE = DATA_DIR + "loans.csv";

    /**
     * Saves all library data to CSV files.
     * @param library the library to save
    */
    public void saveAll(Library library) {
        new File(DATA_DIR).mkdirs();
        saveItems(library);
        saveMembers(library);
        saveLoans(library);
        System.out.println("Data saved successfully.");
    }

    /**
     * Saves all catalogue items to items.csv.
     * @param library the library to save
    */
    private void saveItems(Library library) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ITEMS_FILE, false))) {
            for (LibraryItem item : library.getAllItems()) {
                bw.write(item.getCatalogueEntry());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving items: " + e.getMessage());
        }
    }

    /**
     * Saves all members to members.csv.
     * @param library the library to save
    */
    private void saveMembers(Library library) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBERS_FILE, false))) {
            for (Member m : library.getAllMembers()) {
                bw.write(m.getMemberId() + "," + m.getName() + "," + m.getEmail());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    /**
     * Saves all active and historical loans to loans.csv.
     * @param library the library to save
    */
    private void saveLoans(Library library) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOANS_FILE, false))) {
            for (Loan l : library.getActiveLoans()) {
                bw.write(formatLoan(l));
                bw.newLine();
            }
            for (Loan l : library.getLoanHistory()) {
                bw.write(formatLoan(l));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }
    }

    // loanId,itemId,memberId,borrowDate,dueDate,returnDate(empty if active)
    private String formatLoan(Loan l) {
        return l.getLoanId() + "," + l.getItem().getItemId() + "," + l.getMember().getMemberId() + "," + l.getBorrowDate() + "," + l.getDueDate() + "," + (l.getReturnDate() != null ? l.getReturnDate() : "");
    }
}