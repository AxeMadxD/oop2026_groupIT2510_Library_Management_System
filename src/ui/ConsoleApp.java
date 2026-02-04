package ui;

import controllers.LibraryController;
import domain.book.Book;
import domain.loan.Loan;
import domain.member.Member;
import exceptions.ConflictException;
import exceptions.DatabaseOperationException;
import exceptions.LoanOverdueException;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import domain.report.LoanReport;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final LibraryController controller;
    private final Scanner scanner;
    private Integer lastLoanIdForReturn;

    public ConsoleApp(LibraryController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            lastLoanIdForReturn = null;
            try {
                int choice = readInt("Choose option: ");
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> listAvailableBooks();
                    case 3 -> listAllBooks();
                    case 4 -> findBookById();
                    case 5 -> addMember();
                    case 6 -> listMembers();
                    case 7 -> findMemberById();
                    case 8 -> borrowBook();
                    case 9 -> returnBook();
                    case 10 -> viewCurrentLoansPerMember();
                    case 11 -> viewOverdueLoans();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            } catch (ConflictException e) {
                if (e instanceof LoanOverdueException) {
                    handleOverdueReturn(e.getMessage());
                } else {
                    System.out.println(e.getMessage());
                }
            } catch (DatabaseOperationException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Unexpected error");
            } catch (RuntimeException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1) Add book");
        System.out.println("2) List available books");
        System.out.println("3) List all books");
        System.out.println("4) Find book by id");
        System.out.println("5) Add member");
        System.out.println("6) List members");
        System.out.println("7) Find member by id");
        System.out.println("8) Borrow book");
        System.out.println("9) Return book");
        System.out.println("10) View current loans per member");
        System.out.println("11) View overdue loans");
        System.out.println("0) Exit");
    }

    private void addBook() {
        String type = readRequiredLine("Type (PRINTED / EBOOK / REFERENCE): ", "Type");
        String title = readRequiredLine("Title: ", "Title");
        String author = readRequiredLine("Author: ", "Author");

        Book book = controller.addBook(type, title, author);
        System.out.println("Created: " + book);
    }


    private void listAvailableBooks() {
        List<Book> books = controller.listAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No available books.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private void listAllBooks() {
        List<Book> books = controller.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private void findBookById() {
        int id = readPositiveId("Book id: ");
        Book book = controller.findBookById(id);
        System.out.println(book);
    }

    private void addMember() {
        String fullName = readRequiredLine("Full name: ", "Full name");
        Member member = controller.addMember(fullName);
        System.out.println("Created: " + member);
    }

    private void listMembers() {
        List<Member> members = controller.listMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        for (Member member : members) {
            System.out.println(member);
        }
    }

    private void findMemberById() {
        int id = readPositiveId("Member id: ");
        Member member = controller.findMemberById(id);
        System.out.println(member);
    }

    private void borrowBook() {
        int memberId = readPositiveId("Member id: ");
        int bookId = readPositiveId("Book id: ");
        Loan loan = controller.borrowBook(memberId, bookId);
        System.out.println("Loan created: " + loan);
    }

    private void returnBook() {
        int loanId = readPositiveId("Loan id: ");
        lastLoanIdForReturn = loanId;
        int fine = controller.returnBook(loanId);
        System.out.println("Returned successfully. Fine: " + fine);
    }

    private void viewCurrentLoansPerMember() {
        int memberId = readPositiveId("Member id: ");
        List<Loan> loans = controller.viewCurrentLoansPerMember(memberId);
        if (loans.isEmpty()) {
            System.out.println("No active loans for this member.");
            return;
        }
        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return Integer.parseInt(line.trim());
    }

    private int readPositiveId(String prompt) {
        int id = readInt(prompt);
        if (id <= 0) {
            throw new ValidationException("Id must be positive");
        }
        return id;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readRequiredLine(String prompt, String fieldName) {
        String value = readLine(prompt);
        if (value.isBlank()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
        return value;
    }

    private void handleOverdueReturn(String message) {
        if (lastLoanIdForReturn == null) {
            System.out.println(message);
            return;
        }
        System.out.println(message);
        String answer = readLine("Force return? (y/n): ");
        if (answer.equalsIgnoreCase("y")) {
            try {
                int fine = controller.forceReturnOverdue(lastLoanIdForReturn);
                System.out.println("Returned with fine: " + fine);
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            } catch (DatabaseOperationException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        } else {
            System.out.println("Return canceled.");
        }
    }

    private void viewOverdueLoans() {
        List<LoanReport> reports = controller.viewOverdueLoans();
        if (reports.isEmpty()) {
            System.out.println("No overdue loans.");
            return;
        }
        for (LoanReport report : reports) {
            System.out.println(report.getMember().getFullName() + " - " +
                    report.getBook().getTitle() + " - Fine: " + report.getFine());
        }
    }

}
