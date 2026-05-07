package hr.fesb.java.library.exceptions;

/**
 * Thrown when a lookup is performed for a member ID that does not exist.
 * Checked exception — callers must handle or declare it.
*/

public class MemberNotFoundException extends Exception {

    private final String memberId;

    /**
     * @param memberId the ID that was not found
    */
    public MemberNotFoundException(String memberId) {
        super("No member found with ID : " + memberId);
        this.memberId = memberId;
    }

    /**
     * @return the member ID that triggered this exception
    */
    public String getMemberId() {
        return memberId;
    }
}