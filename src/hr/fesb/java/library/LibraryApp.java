package hr.fesb.java.library;

import hr.fesb.java.library.gui.LibraryGUI;
import hr.fesb.java.library.io.LibraryFileManager;
import hr.fesb.java.library.model.*;
import javax.swing.*;

/**
 * Main entry point for the Library Information System.
 ***/
public class LibraryApp {

    public static void main(String[] args) {

        Library library = new Library();
        LibraryFileManager fm = new LibraryFileManager();

        fm.loadAll(library);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fm.saveAll(library);
        }));

        SwingUtilities.invokeLater(() -> new LibraryGUI(library));
    }
}