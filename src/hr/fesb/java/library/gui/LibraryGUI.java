package hr.fesb.java.library.gui;

import hr.fesb.java.library.model.*;
import javax.swing.*;

/**
* Main Swing window for the Library Information System.
*/
public class LibraryGUI extends JFrame {

    private Library library;
    private JTabbedPane tabbedPane;
    private CataloguePanel cataloguePanel;
    private MembersPanel membersPanel;
    private LoansPanel loansPanel;

    /**
     * @param library the library instance to display
     */
    public LibraryGUI(Library library) {
        this.library = library;
        setTitle("Library Information System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        initComponents();
    }

    /**
     * Initialises and lays out all GUI components.
    */
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        cataloguePanel = new CataloguePanel(library);
        membersPanel = new MembersPanel(library);
        loansPanel = new LoansPanel(library);
        tabbedPane.addTab("Catalogue", cataloguePanel);
        tabbedPane.addTab("Members", membersPanel);
        tabbedPane.addTab("Loans", loansPanel);
        add(tabbedPane);
        setVisible(true);
    }

    /**
     * Refreshes all tabs with latest data.
    */
    public void refreshAll() {
        cataloguePanel.refresh();
        membersPanel.refresh();
        loansPanel.refresh();
    }
}