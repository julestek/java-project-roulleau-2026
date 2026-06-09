package hr.fesb.java.library.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library member who can borrow items.
 */
public class Member {

    private String memberId;
    private String name;
    private String email;
    private List<Loan> borrowingHistory;

    /**
    * @param memberId unique identifier (non-empty)
    * @param name full name (non-empty)
    * @param email email address (can be empty)
    * @throws IllegalArgumentException if memberId or name is empty
    */
    public Member(String memberId, String name, String email) {
        if (memberId == null || memberId.trim().isEmpty())
            throw new IllegalArgumentException("Member ID cannot be empty.");
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        this.memberId = memberId.trim();
        this.name = name.trim();
        this.email = (email != null) ? email.trim() : "";
        this.borrowingHistory = new ArrayList<>();
    }

    /**
     * Adds a loan to this member's borrowing history.
     * @param loan the loan to add
    */
    public void addLoan(Loan loan) {
        if (loan != null) borrowingHistory.add(loan);
    }

    /**
     * @return list of loans that have not been returned yet
    */
    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan l : borrowingHistory)
            if (!l.isReturned()) active.add(l);
        return active;
    }

    /**
     * @return all loans, both active and completed
    */
    public List<Loan> getBorrowingHistory() {
        return new ArrayList<>(borrowingHistory);
    }

    // --- Getters ---
    public String getMemberId() { return memberId;}
    public String getName() { return name;}
    public String getEmail() { return email;}

    // --- Setters ---
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name.trim();
    }
    public void setEmail(String email) {
        this.email = (email != null) ? email.trim() : "";
    }

    @Override
    public String toString() {
        return String.format("Member[%s] %s <%s>, %d active loan(s)",
            memberId, name, email, getActiveLoans().size());
    }
}