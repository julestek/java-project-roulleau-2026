package hr.fesb.java.library.gui;

import hr.fesb.java.library.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel displaying the list of members and their loan history.
*/
public class MembersPanel extends JPanel {

    private Library library;
    private DefaultListModel<Member> listModel;
    private JList<Member> memberList;
    private JTextArea detailArea;

    /**
     * @param library the library instance to display
    */
    public MembersPanel(Library library) {
        this.library = library;
        setLayout(new BorderLayout());
        initComponents();
        refresh();
    }

    private void initComponents() {
        // Left -- member list
        listModel = new DefaultListModel<>();
        memberList = new JList<>(listModel);
        memberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Member) {
                    Member m = (Member) value;
                    setText(m.getName() + " [" + m.getMemberId() + "]");
                }
                return this;
            }
        });
        JScrollPane listScroll = new JScrollPane(memberList);
        listScroll.setPreferredSize(new Dimension(250, 0));

        detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane detailScroll = new JScrollPane(detailArea);

        add(listScroll, BorderLayout.WEST);
        add(detailScroll, BorderLayout.CENTER);

        // Selection listener
        memberList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Member selected = memberList.getSelectedValue();
                if (selected != null) showMemberDetails(selected);
            }
        });
    }

    private void showMemberDetails(Member member) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name    : ").append(member.getName()).append("\n");
        sb.append("ID      : ").append(member.getMemberId()).append("\n");
        sb.append("Email   : ").append(member.getEmail()).append("\n\n");
        sb.append("Active loans:\n");
        List<Loan> active = member.getActiveLoans();
        if (active.isEmpty()) {
            sb.append("  No active loans.\n");
        } else {
            for (Loan l : active)
                sb.append("  ").append(l).append("\n");
        }
        sb.append("\nBorrowing history:\n");
        List<Loan> history = member.getBorrowingHistory();
        if (history.isEmpty()) {
            sb.append("  No borrowing history.\n");
        } else {
            for (Loan l : history)
                sb.append("  ").append(l).append("\n");
        }
        detailArea.setText(sb.toString());
    }

    /** Refreshes the member list with latest data.
    */
    public void refresh() {
        listModel.clear();
        for (Member m : library.getAllMembers())
            listModel.addElement(m);
    }
}