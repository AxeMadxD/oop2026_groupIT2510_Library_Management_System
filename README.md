Ardak, Akhmad 
IT-2025


Library Management System

A Java-based console application for managing a library, supporting books, members, loans, and fines. The project demonstrates OOP concepts, design patterns, and modern Java features such as generics, lambdas, and streams.

Features

Book Management

Add, list, and find books

Supports multiple book types via Factory Pattern (PrintedBook, Ebook, ReferenceBook)

Track availability

Member Management

Add, list, and find library members

Loan Management

Borrow and return books

Track current and overdue loans

Calculate fines using Singleton Pattern (FinePolicy)

Generate detailed loan reports with Builder Pattern (LoanReport)

Generate overdue loan reports

Functional Programming

Filtering and sorting of loans and books using lambdas and streams

Persistence

JDBC-based repositories for Book, Member, and Loan

PostgreSQL database with tables: books, members, loans

Design Patterns Used

Singleton: FinePolicy ensures only one fine policy exists.

Builder: LoanReport.Builder for flexible construction of loan reports.

Factory: BookFactory to create different book types dynamically.

Functional Interfaces / Lambdas: Used for filtering and sorting collections.

Project Structure
src/
 ├─ controllers/       # LibraryController
 ├─ domain/            # Entities: Book, Loan, Member
 ├─ repositories/      # Repository interfaces
 ├─ repositories.jdbc/ # JDBC implementations
 ├─ services/          # Business logic, LoanService, FineCalculator
 ├─ services.singleton/# Singleton FinePolicy
 ├─ reports/           # LoanReport with Builder
 ├─ factories/         # BookFactory
 ├─ db/                # Database setup
 └─ ui/                # ConsoleApp (CLI)

Database Setup

The project uses a PostgreSQL database. Tables are created automatically via DbInitializer:

books: id, title, author, available, type

members: id, full_name

loans: id, book_id, member_id, loan_date, due_date, return_date

How to Run

Configure database connection in Database.java.

Run DbInitializer.init() to create tables.

Start the console app:

ConsoleApp app = new ConsoleApp(controller);
app.run();


Use the menu to add books, members, borrow/return books, and generate reports.

Notes

Overdue fines are calculated using the Singleton FinePolicy.

Loan reports are built using Builder pattern.

Book types are handled via Factory pattern.

Streams and lambdas are used for filtering and sorting.
