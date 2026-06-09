package hr.fesb.java.library.model;

import hr.fesb.java.library.exceptions.ItemNotAvailableException;
import hr.fesb.java.library.exceptions.MemberNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.time.LocalDate;

/**
 * Central business logic class for the library system.
 * The GUI must only call methods on this class.
*/
public class Library {

    private HashMap<String, LibraryItem> catalogue;
    private HashMap<String, Member> members;
    private TreeSet<Loan> activeLoans;
    private List<Loan> loanHistory;

    /**
     * Creates an empty library.
    */
    public Library() {
        catalogue = new HashMap<>();
        members = new HashMap<>();
        activeLoans = new TreeSet<>();
        loanHistory = new ArrayList<>();
    }

    /**
     * Adds an item to the catalogue.
     * @param item the item to add (non-null)
     * @throws IllegalArgumentException if item is null
    */
    public void addItem(LibraryItem item) {
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null.");
        catalogue.put(item.getItemId(), item);
    }

    /**
     * Removes an item from the catalogue by ID.
     * @param itemId the ID of the item to remove
    */
    public void removeItem(String itemId) {
        catalogue.remove(itemId);
    }

    /**
     * @param itemId the item ID to look up
     * @return the item, or null if not found
    */
    public LibraryItem findItemById(String itemId) {
        return catalogue.get(itemId);
    }

    /**
     * @return unmodifiable view of all items in the catalogue
     */
    public Collection<LibraryItem> getAllItems() {
        return Collections.unmodifiableCollection(catalogue.values());
    }

    /**
     * Searches and filters the catalogue.
     * @param query text to search in title, author, etc. null means no filter
     * @param type class simple name such as Book or DVD. null means all types
     * @param availableOnly if true, only returns items with copies available
     * @return sorted list of matching items, alphabetical by title
    */
    public List<LibraryItem> searchItems(String query, String type, boolean availableOnly) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : catalogue.values()) {
            if (type != null && !type.isEmpty() && !item.getClass().getSimpleName().equalsIgnoreCase(type))
                continue;
            if (availableOnly && !item.isAvailable())
                continue;
            if (query != null && !query.trim().isEmpty() && !item.matchesQuery(query))
                continue;
            results.add(item);
        }
        results.sort(Comparator.comparing(LibraryItem::getTitle, String.CASE_INSENSITIVE_ORDER));
        return results;
    }

    /**
     * Registers a new member.
     * @param member the member to register (non-null)
     * @throws IllegalArgumentException if member is null
    */
    public void registerMember(Member member) {
        if (member == null)
            throw new IllegalArgumentException("Member cannot be null.");
        members.put(member.getMemberId(), member);
    }

    /**
     * Finds a member by ID.
     * @param memberId the member ID to look up
     * @return the member
     * @throws MemberNotFoundException if no member has this ID
    */
    public Member findMemberById(String memberId) throws MemberNotFoundException {
        Member m = members.get(memberId);
        if (m == null)
            throw new MemberNotFoundException(memberId);
        return m;
    }

    /**
     * @return all members sorted alphabetically by name
    */
    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>(members.values());
        list.sort(Comparator.comparing(Member::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    // Package-private access for LibraryFileManager
    public HashMap<String, LibraryItem> getCatalogue() { return catalogue; }
    public HashMap<String, Member> getMembers() { return members; }
    public TreeSet<Loan> getActiveLoanSet() { return activeLoans; }
    public List<Loan> getLoanHistory() { return loanHistory; }

    /**
     * Creates a new loan for the given member and item.
     * @param memberId the ID of the member borrowing the item
     * @param itemId the ID of the item to borrow
     * @return the created loan
     * @throws MemberNotFoundException if the member ID is unknown
     * @throws ItemNotAvailableException if the item has no copies left
    */
    public Loan borrowItem(String memberId, String itemId)
            throws MemberNotFoundException,
                   hr.fesb.java.library.exceptions.ItemNotAvailableException {
        Member member = findMemberById(memberId);
        LibraryItem item = catalogue.get(itemId);
        if (item == null)
            throw new IllegalArgumentException("Item not found: " + itemId);
        item.borrowItem();
        String loanId = "L" + System.currentTimeMillis();
        Loan loan = new Loan(loanId, item, member, LocalDate.now());
        activeLoans.add(loan);
        member.addLoan(loan);
        return loan;
    }

    /**
     * Closes a loan and restores item availability.
     * @param loanId the ID of the loan to close
     * @throws IllegalArgumentException if the loan ID is not found
    */
    public void returnItem(String loanId) {
        Loan toReturn = null;
        for (Loan l : activeLoans) {
            if (l.getLoanId().equals(loanId)) {
                toReturn = l;
                break;
            }
        }
        if (toReturn == null)
            throw new IllegalArgumentException("Active loan not found: " + loanId);
        toReturn.setReturnDate(LocalDate.now());
        toReturn.getItem().returnItem();
        activeLoans.remove(toReturn);
        loanHistory.add(toReturn);
    }

    /**
     * @return list of all currently active loans
    */
    public List<Loan> getActiveLoans() {
        return new ArrayList<>(activeLoans);
    }

    /**
     * @return list of active loans that are overdue
    */
    public List<Loan> getOverdueLoans() {
        List<Loan> overdue = new ArrayList<>();
        for (Loan l : activeLoans)
            if (l.isOverdue()) overdue.add(l);
        return overdue;
    }

    /**
     * @return count of currently overdue loans
    */
    public int getOverdueCount() {
        return getOverdueLoans().size();
    }

    /**
     * @param memberId the member ID to look up
     * @return all loans for this member
     * @throws MemberNotFoundException if the member ID is unknown
    */
    public List<Loan> getLoansByMember(String memberId) throws MemberNotFoundException {
        return findMemberById(memberId).getBorrowingHistory();
    }
}