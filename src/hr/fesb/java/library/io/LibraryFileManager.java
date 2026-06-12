package hr.fesb.java.library.io;

import hr.fesb.java.library.model.*;
import java.io.*;
import java.time.LocalDate;

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

    /**
     * Loads all library data from CSV files.
     * @param library the library to load data into
    */
    public void loadAll(Library library) {
        loadItems(library);
        loadMembers(library);
        loadLoans(library);
        System.out.println("Data loaded successfully.");
    }

    /**
     * Loads all items from items.csv.
     * @param library the library to load items into
     */
    private void loadItems(Library library) {
        File f = new File(ITEMS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                LibraryItem item = parseItem(p);
                if (item != null) library.addItem(item);
            }
        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
    }

    /**
     * Parses a CSV line into a LibraryItem.
     * @param p the CSV fields
     * @return the parsed item, or null if the line is invalid
     */
    private LibraryItem parseItem(String[] p) {
        try {
            switch (p[0].toUpperCase()) {
                case "BOOK":
                    return restoreCopies(
                        new Book(p[1], p[2], p[3], p[4], Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7])),
                        Integer.parseInt(p[8]));
                case "MAGAZINE":
                    return restoreCopies(
                        new Magazine(p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7])),
                        Integer.parseInt(p[8]));
                case "DVD":
                    return restoreCopies(
                        new DVD(p[1], p[2], p[3], Integer.parseInt(p[4]), Integer.parseInt(p[5]), AgeRating.valueOf(p[6]), Integer.parseInt(p[7])),
                        Integer.parseInt(p[8]));
                case "AUDIOBOOK":
                    return restoreCopies(
                        new Audiobook(p[1], p[2], p[3], Double.parseDouble(p[4]), Integer.parseInt(p[5]), AudioFormat.valueOf(p[6]), Integer.parseInt(p[7])),
                        Integer.parseInt(p[8]));
                default:
                    System.err.println("Unknown item type: " + p[0]);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Skipping invalid item line: " + e.getMessage());
            return null;
        }
    }

    /**
     * Restores the exact copies available after loading from CSV.
     * @param item the item to restore
     * @param copiesAvailable the number of copies available
     * @return the item with restored copies
     */
    private LibraryItem restoreCopies(LibraryItem item, int copiesAvailable) {
        item.setCopiesAvailable(copiesAvailable);
        return item;
    }

    /**
     * Loads all members from members.csv.
     * @param library the library to load members into
     */
    private void loadMembers(Library library) {
        File f = new File(MEMBERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                library.registerMember(new Member(p[0], p[1], p.length > 2 ? p[2] : ""));
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
    }

    /**
     * Loads all loans from loans.csv.
     * @param library the library to load loans into
     */
    private void loadLoans(Library library) {
        File f = new File(LOANS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                LibraryItem item = library.findItemById(p[1]);
                if (item == null) continue;
                Member member;
                try {
                    member = library.findMemberById(p[2]);
                } catch (hr.fesb.java.library.exceptions.MemberNotFoundException e) {
                    continue;
                }
                Loan loan = new Loan(p[0], item, member, LocalDate.parse(p[3]));
                if (p.length > 5 && !p[5].isEmpty()) {
                    loan.setReturnDate(LocalDate.parse(p[5]));
                    library.getLoanHistory().add(loan);
                } else {
                    library.getActiveLoanSet().add(loan);
                }
                member.addLoan(loan);
            }
        } catch (Exception e) {
            System.err.println("Error loading loans: " + e.getMessage());
        }
    }
}