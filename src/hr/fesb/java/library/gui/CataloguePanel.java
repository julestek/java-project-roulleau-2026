package hr.fesb.java.library.gui;

import hr.fesb.java.library.model.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *Panel displaying the library catalogue with search and filter.*/
public class CataloguePanel extends JPanel {

    private Library library;
    private JTextField searchField;
    private JComboBox<String> typeCombo;
    private JCheckBox availableCheck;
    private JTable table;
    private CatalogueTableModel tableModel;

    /**
     * @param library the library instance to display
    */
    public CataloguePanel(Library library) {
        this.library = library;
        setLayout(new BorderLayout());
        initComponents();
        refresh();
    }

    private void initComponents() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        typeCombo = new JComboBox<>(new String[]{"All", "Book", "Magazine", "DVD", "Audiobook"});
        availableCheck = new JCheckBox("Available only");
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Type:"));
        searchPanel.add(typeCombo);
        searchPanel.add(availableCheck);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        tableModel = new CatalogueTableModel();
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> search());

        resetButton.addActionListener(e -> {
            searchField.setText("");
            typeCombo.setSelectedIndex(0);
            availableCheck.setSelected(false);
            refresh();
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        LibraryItem item = tableModel.getItemAt(row);
                        JOptionPane.showMessageDialog(
                            CataloguePanel.this,
                            item.getSummary() + "\n\n" + item.getCatalogueEntry(),
                            "Item Details",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });
    }

    private void search() {
        String query = searchField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        if ("All".equals(type)) type = null;
        boolean availableOnly = availableCheck.isSelected();
        List<LibraryItem> results = library.searchItems(query, type, availableOnly);
        tableModel.setItems(results);
    }

    /**
    * Refreshes the table with all items.
    */
    public void refresh() {
        tableModel.setItems(new ArrayList<>(library.getAllItems()));
    }
}

/**
 * Table model for the catalogue table.
*/
class CatalogueTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {"Type", "Title", "Author / Director", "Year", "Available"};
    private List<LibraryItem> items = new ArrayList<>();

    public void setItems(List<LibraryItem> items) {
        this.items = new ArrayList<>(items);
        fireTableDataChanged();
    }

    public LibraryItem getItemAt(int row) {
        return items.get(row);
    }

    @Override
    public int getRowCount() { return items.size(); }

    @Override
    public int getColumnCount() { return COLUMNS.length; }

    @Override
    public String getColumnName(int col) { return COLUMNS[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        LibraryItem item = items.get(row);
        switch (col) {
            case 0: return item.getClass().getSimpleName();
            case 1: return item.getTitle();
            case 2: return getAuthorOrDirector(item);
            case 3: return item.getPublicationYear();
            case 4: return item.getCopiesAvailable() + "/" + item.getTotalCopies();
            default: return "";
        }
    }

    private String getAuthorOrDirector(LibraryItem item) {
        if (item instanceof Book) return ((Book) item).getAuthor();
        if (item instanceof DVD) return ((DVD) item).getDirector();
        if (item instanceof Magazine) return ((Magazine) item).getPublisher();
        if (item instanceof Audiobook) return ((Audiobook) item).getNarrator();
        return "";
    }
}