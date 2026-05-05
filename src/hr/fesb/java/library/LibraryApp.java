package hr.fesb.java.library;

import hr.fesb.java.library.model.LibraryItem;

public class LibraryApp {
    public static void main(String[] args) {

        // Test 1 — titre vide doit planter
        try {
            LibraryItem item = new LibraryItem("ID001", "", 2020, 1) {
                public String getCatalogueEntry() { return ""; }
                public String getSummary() { return ""; }
                public boolean matchesQuery(String q) { return false; }
                public String getSearchableText() { return ""; }
            };
        } catch (IllegalArgumentException e) {
            System.out.println("Test 1 OK — titre vide rejeté : " + e.getMessage());
        }

        // Test 2 — année invalide
        try {
            LibraryItem item = new LibraryItem("ID002", "Mon livre", 2099, 1) {
                public String getCatalogueEntry() { return ""; }
                public String getSummary() { return ""; }
                public boolean matchesQuery(String q) { return false; }
                public String getSearchableText() { return ""; }
            };
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2 OK — année invalide rejetée : " + e.getMessage());
        }

        // Test 3 — 0 copies
        try {
            LibraryItem item = new LibraryItem("ID003", "Mon livre", 2020, 0) {
                public String getCatalogueEntry() { return ""; }
                public String getSummary() { return ""; }
                public boolean matchesQuery(String q) { return false; }
                public String getSearchableText() { return ""; }
            };
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 OK — 0 copies rejeté : " + e.getMessage());
        }

        // Test 4 — objet valide
        LibraryItem item = new LibraryItem("ID004", "1984", 1949, 3) {
            public String getCatalogueEntry() { return "BOOK,ID004,1984"; }
            public String getSummary() { return "Roman de Orwell"; }
            public boolean matchesQuery(String q) { return false; }
            public String getSearchableText() { return "1984"; }
        };
        System.out.println("Test 4 OK — toString : " + item);
        System.out.println("Test 4 OK — isAvailable : " + item.isAvailable());

        // Test 5 — emprunter jusqu'à épuisement
        try {
            item.borrowItem();
            item.borrowItem();
            item.borrowItem();
            item.borrowItem(); // doit planter
        } catch (hr.fesb.java.library.exceptions.ItemNotAvailableException e) {
            System.out.println("Test 5 OK — plus de copie : " + e.getMessage());
        }

        // Test 6 — retourner un exemplaire
        item.returnItem();
        System.out.println("Test 6 OK — après retour, dispo : " + item.getCopiesAvailable());
    }
}