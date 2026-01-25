# Library Management System (Milestone 1)

## 1) Project Overview
A Java 17 console Library Management System for managing books, members, categories, loans, and returns for a library/reading room. The project uses JDBC with PostgreSQL (Supabase supported) and follows SOLID layering.

## 2) Features (Milestone 1)
- Entities: Category, Book, Member, Loan
- User stories:
  - Borrow a book
  - Return a book
  - View current loans per member
  - List available books
  - Create/list/find by id for Category/Book/Member

## 3) Database Design
Tables:
- `categories`
  - `id` SERIAL PRIMARY KEY
  - `name` VARCHAR(100) UNIQUE NOT NULL
- `books`
  - `id` SERIAL PRIMARY KEY
  - `category_id` INT NOT NULL REFERENCES `categories(id)`
  - `title` VARCHAR(255) NOT NULL
  - `author` VARCHAR(255) NOT NULL
  - `available` BOOLEAN NOT NULL DEFAULT TRUE
- `members`
  - `id` SERIAL PRIMARY KEY
  - `email` VARCHAR(255) UNIQUE NOT NULL
  - `full_name` VARCHAR(255) NOT NULL
- `loans`
  - `id` SERIAL PRIMARY KEY
  - `book_id` INT NOT NULL REFERENCES `books(id)`
  - `member_id` INT NOT NULL REFERENCES `members(id)`
  - `loan_date` DATE NOT NULL
  - `due_date` DATE NOT NULL
  - `return_date` DATE NULL

Foreign keys:
- `books.category_id` -> `categories.id`
- `loans.book_id` -> `books.id`
- `loans.member_id` -> `members.id`

`return_date` is nullable and is used to detect active loans (current loans have `return_date IS NULL`).

## 4) Architecture (SOLID)
Layered design with clear responsibilities:
- `domain/` = entity classes only
- `repositories/` = repository interfaces
- `repositories/jdbc/` = JDBC implementations with SQL
- `services/` = business logic (`LoanService`, `FineCalculator`)
- `controllers/` = application layer without SQL
- `ui/` = `ConsoleApp` menu and input handling
- `db/` = `Database` interface, `PostgresDatabase`, `DbInitializer`
- `config/` = `DbConfig` (env variables or hardcoded switch)

SOLID highlights:
- SRP: each class has one responsibility (UI, service, repository, domain)
- DIP: `Database` and repository interfaces decouple logic from JDBC
- Controllers contain no SQL
- `FineCalculator` separated from `LoanService` for flexibility

## 5) Exception Handling
Custom exceptions and typical triggers:
- `MemberNotFoundException` when member id does not exist
- `BookNotFoundException` when book id does not exist
- `CategoryNotFoundException` when category id does not exist
- `LoanNotFoundException` when loan id does not exist
- `BookAlreadyOnLoanException` when borrowing a non-available book
- `LoanOverdueException` when returning a loan past the due date

Overdue return flow:
- The UI catches `LoanOverdueException`, prompts “Force return? (y/n)”, and if confirmed, performs a forced return and prints the fine.

## 6) How to Run (IntelliJ + Maven)
Requirements:
- Java 17
- Maven
- Internet access if using Supabase

Steps:
1. Open the project in IntelliJ.
2. Run `src/Main.java`.
3. `DbInitializer` creates tables automatically on startup.

## 7) Configure Database Connection
Two modes are supported.

A) Environment Variables (recommended)
- `DB_URL`, `DB_USER`, `DB_PASSWORD`
- Example for Supabase session pooler:

```bash
DB_URL=jdbc:postgresql://<POOLER_HOST>:5432/postgres?sslmode=require
DB_USER=postgres.<project-ref>
DB_PASSWORD=******
```

B) Hardcoded mode (optional)
- Switch one line in `src/config/DbConfig.java` between `EnvDbSettings` and `HardcodedDbSettings`.
- Warning: do NOT commit real passwords into public git.

## 8) Console Menu
Menu options:
1) Add category
2) List categories
3) Find category by id
4) Add book
5) List available books
6) List all books
7) Find book by id
8) Add member
9) List members
10) Find member by id
11) Borrow book
12) Return book
13) View current loans per member
0) Exit

## 9) Notes for Defense
- JDBC + repository pattern keeps SQL in one layer and supports testing/maintenance.
- `due_date` and `return_date` drive loan status and fine logic.
- Example FK: `books.category_id` references `categories.id`, and adding a book validates the category.
- “Current loans” means `return_date IS NULL`.
