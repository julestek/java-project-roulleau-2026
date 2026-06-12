# Library Information System — Java Project 2

A Java desktop application that simulates a library catalogue and lending system.
Library staff can manage items (books, magazines, DVDs, audiobooks), register
members, process loans and returns, and search the catalogue. All data persists
between runs using CSV files.

**Name:** Jules Christophe Ludovic Roulleau
**Course:** Programming in Java (FELP11)
**University:** University of Split — FESB
**Academic Year:** 2025/2026
**Professor :** Assoc. Prof. Vladimir Pleština, PhD

## Features

- Manage the catalogue (add, find, remove items) with full field validation
- Register members and view their profile and borrowing history
- Borrow and return items, with automatic due dates (borrow date + 14 days)
- Overdue detection with a fine of 0.20 EUR per day late
- Search and filter the catalogue by text, type and availability
- Three-tab Swing GUI: Catalogue, Members, Loans (overdue rows highlighted in red)
- CSV persistence: all data is saved on exit and reloaded on startup

## Project structure

src/    All Java source files, organised in packages
data/   Sample CSV data files (items, members, loans)
docs/   Written report (PDF) and UML diagram

## Requirements

- Java 17 or higher (developed and tested with OpenJDK 21)

## How to compile

```bash
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
```

On Windows:

```cmd
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
```

## How to run

```bash
java -cp bin hr.fesb.java.library.LibraryApp
```

## Known issues and limitations

- The dynamic "Add Item" dialog was not implemented. Items are loaded from the
  CSV data files, which act as the demonstration catalogue. Add, edit and remove
  operations are fully supported at the logic level in the Library class.
- The optional background thread that updates an overdue badge was not
  implemented. Overdue detection itself works and is recalculated on each refresh.
