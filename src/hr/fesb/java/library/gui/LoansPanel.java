package hr.fesb.java.library.gui;

import hr.fesb.java.library.model.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel displaying all active loans with overdue highlighting.*/
public class LoansPanel extends JPanel {

    private Library library;
    private JTable table;
    private LoansTableModel tableModel;

    /**
     * @param library the library instance to display
    */
    public LoansPanel(Library library) {
        this.library = library;
        setLayout(new BorderLayout());
        initComponents();
        refresh();
    }

    private void initComponents() {
        tableModel = new LoansTableModel();
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                Loan loan = tableModel.getLoanAt(row);
                if (loan.isOverdue()) {
                    c.setBackground(new Color(255, 180, 180));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton borrowButton = new JButton("Borrow Item");
        JButton returnButton = new JButton("Return Item");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        borrowButton.addActionListener(e -> showBorrowDialog());
        returnButton.addActionListener(e -> showReturnDialog());
        refreshButton.addActionListener(e -> refresh());
    }

    private void showBorrowDialog() {
        JTextField memberIdField = new JTextField(15);
        JTextField itemIdField = new JTextField(15);
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Member ID:"));
        panel.add(memberIdField);
        panel.add(new JLabel("Item ID:"));
        panel.add(itemIdField);
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Borrow Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String memberId = memberIdField.getText().trim();
            String itemId = itemIdField.getText().trim();
            try {
                Loan loan = library.borrowItem(memberId, itemId);
                JOptionPane.showMessageDialog(this,"Loan confirmed.\nDue date: " + loan.getDueDate(),"Success",JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } catch (hr.fesb.java.library.exceptions.MemberNotFoundException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (hr.fesb.java.library.exceptions.ItemNotAvailableException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showReturnDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a loan from the table first.",
                "No loan selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        Loan loan = tableModel.getLoanAt(selectedRow);
        int result = JOptionPane.showConfirmDialog(this, "Return \"" + loan.getItem().getTitle() + "\" for " + loan.getMember().getName() + " ?", "Confirm Return", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            library.returnItem(loan.getLoanId());
            String message = "Item returned successfully.";
            if (loan.getFineAmount() > 0)
                message += String.format("\nOverdue fine: %.2f EUR", loan.getFineAmount());
            JOptionPane.showMessageDialog(this, message, "Return Confirmed", JOptionPane.INFORMATION_MESSAGE);
            refresh();
        }
    }

    /**
     * Refreshes the loans table with latest data.
*/
    public void refresh() {
        tableModel.setLoans(library.getActiveLoans());
    }
}

/**
* Table model for the loans table.
*/
class LoansTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {"Member", "Item", "Borrow Date", "Due Date", "Status", "Fine"};
    private List<Loan> loans = new ArrayList<>();

    public void setLoans(List<Loan> loans) {
        this.loans = new ArrayList<>(loans);
        fireTableDataChanged();
    }

    public Loan getLoanAt(int row) {
        return loans.get(row);
    }

    @Override
    public int getRowCount() { return loans.size(); }

    @Override
    public int getColumnCount() { return COLUMNS.length; }

    @Override
    public String getColumnName(int col) { return COLUMNS[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        Loan loan = loans.get(row);
        switch (col) {
            case 0: return loan.getMember().getName();
            case 1: return loan.getItem().getTitle();
            case 2: return loan.getBorrowDate();
            case 3: return loan.getDueDate();
            case 4: return loan.isOverdue() ? "OVERDUE " + loan.getDaysOverdue() + " day(s)" : "Active";
            case 5: return loan.getFineAmount() > 0 ? String.format("%.2f EUR", loan.getFineAmount()) : "-";
            default: return "";
        }
    }
}