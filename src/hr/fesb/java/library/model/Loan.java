package hr.fesb.java.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a single borrowing transaction.
 * Implements Comparable to allow sorting by due date in a TreeSet.
*/
public class Loan implements Comparable<Loan> {

    private static final double FINE_PER_DAY = 0.20;

    private String      loanId;
    private LibraryItem item;
    private Member      member;
    private LocalDate   borrowDate;
    private LocalDate   dueDate;
    private LocalDate   returnDate;

    /**
     * @param loanId     unique identifier (non-empty)
     * @param item       the borrowed item (non-null)
     * @param member     the borrowing member (non-null)
     * @param borrowDate the date the item was borrowed (non-null)
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Loan(String loanId, LibraryItem item,
                Member member, LocalDate borrowDate) {
        if (loanId == null || loanId.trim().isEmpty())
            throw new IllegalArgumentException("Loan ID cannot be empty.");
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null.");
        if (member == null)
            throw new IllegalArgumentException("Member cannot be null.");
        if (borrowDate == null)
            throw new IllegalArgumentException("Borrow date cannot be null.");
        this.loanId     = loanId.trim();
        this.item       = item;
        this.member     = member;
        this.borrowDate = borrowDate;
        this.dueDate    = borrowDate.plusDays(14);
        this.returnDate = null;
    }

    /**
     * @return true if the due date has passed and item has not been returned
     */
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    /**
     * @return number of days overdue, 0 if not overdue
     */
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * @return fine amount in EUR (0.20 per day late), 0.00 if not overdue
     */
    public double getFineAmount() {
        return getDaysOverdue() * FINE_PER_DAY;
    }

    /**
     * @return true if this loan has been closed
     */
    public boolean isReturned() {
        return returnDate != null;
    }

    /**
     * Closes this loan by recording the return date.
     * @param date the date the item was returned
     */
    public void setReturnDate(LocalDate date) {
        this.returnDate = date;
    }

    /**
     * Sorts loans by due date ascending — used by TreeSet.
     * Tie-break on loanId to ensure consistency.
     */
    @Override
    public int compareTo(Loan other) {
        int cmp = this.dueDate.compareTo(other.dueDate);
        if (cmp != 0) return cmp;
        return this.loanId.compareTo(other.loanId);
    }

    // --- Getters ---
    public String      getLoanId()     { return loanId; }
    public LibraryItem getItem()       { return item; }
    public Member      getMember()     { return member; }
    public LocalDate   getBorrowDate() { return borrowDate; }
    public LocalDate   getDueDate()    { return dueDate; }
    public LocalDate   getReturnDate() { return returnDate; }

    @Override
    public String toString() {
        String status = isReturned()
            ? "returned on " + returnDate
            : isOverdue()
                ? String.format("OVERDUE %d day(s), fine: %.2f EUR",
                    getDaysOverdue(), getFineAmount())
                : "due " + dueDate;
        return String.format("Loan[%s] %s --> \"%s\" | %s",
            loanId, member.getName(), item.getTitle(), status);
    }
}