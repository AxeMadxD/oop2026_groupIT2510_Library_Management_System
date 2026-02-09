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
import java.util.Optional;
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
                    case 11 -> viewLoanReport();
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
        System.out.println("11) Build loan report by member");
        System.out.println("0) Exit");
    }

    private void addBook() {
        String type = readBookType();
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
        if (book == null) {
            System.out.println("Book not found: " + id);
            return;
        }
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
        if (member == null) {
            System.out.println("Member not found: " + id);
            return;
        }
        System.out.println(member);
    }

    private void borrowBook() {
        int memberId = selectMemberIdByName();
        if (memberId == -1) {
            return;
        }
        int bookId = selectAvailableBookIdByTitle();
        if (bookId == -1) {
            return;
        }
        Loan loan = controller.borrowBook(memberId, bookId);
        System.out.println("Loan created: " + loan);
    }

    private void returnBook() {
        int loanId = readPositiveId("Loan id: ");
        lastLoanIdForReturn = loanId;
        controller.returnBook(loanId);
        System.out.println("Returned successfully.");
    }

    private void viewCurrentLoansPerMember() {
        int memberId = selectMemberIdByName();
        if (memberId == -1) {
            return;
        }
        List<Loan> loans = controller.viewCurrentLoansPerMember(memberId);
        if (loans.isEmpty()) {
            System.out.println("No active loans for this member.");
            return;
        }
        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private void viewLoanReport() {
        int memberId = selectMemberIdByName();
        if (memberId == -1) {
            return;
        }
        Optional<LoanReport> reportOpt = controller.buildLoanReport(memberId);
        if (reportOpt.isEmpty()) {
            System.out.println("No report found.");
            return;
        }
        LoanReport report = reportOpt.get();
        if (report.getMember() != null && report.getBook() != null) {
            System.out.println(report.getMember().getFullName() + " - " +
                    report.getBook().getTitle() + " - Fine: " + report.getFine());
        } else {
            System.out.println("Report generated.");
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

    private String readBookType() {
        System.out.println("Book type:");
        System.out.println("1) Printed");
        System.out.println("2) Ebook");
        System.out.println("3) Reference");
        int choice = readInt("Choose type (1-3): ");
        return switch (choice) {
            case 1 -> "PRINTED";
            case 2 -> "EBOOK";
            case 3 -> "REFERENCE";
            default -> throw new ValidationException("Type must be 1, 2, or 3");
        };
    }

    private int selectMemberIdByName() {
        String name = readRequiredLine("Member full name: ", "Member full name");
        List<Member> members = controller.listMembers();
        List<Member> matches = members.stream()
                .filter(m -> m.getFullName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        if (matches.isEmpty()) {
            System.out.println("No members found matching: " + name);
            return -1;
        }
        if (matches.size() == 1) {
            return matches.get(0).getId();
        }
        System.out.println("Multiple members found:");
        for (int i = 0; i < matches.size(); i++) {
            Member m = matches.get(i);
            System.out.println((i + 1) + ") " + m.getFullName() + " (id=" + m.getId() + ")");
        }
        int choice = readInt("Choose member (1-" + matches.size() + "): ");
        if (choice < 1 || choice > matches.size()) {
            throw new ValidationException("Member choice out of range");
        }
        return matches.get(choice - 1).getId();
    }

    private int selectAvailableBookIdByTitle() {
        String title = readRequiredLine("Book title: ", "Book title");
        List<Book> books = controller.listAvailableBooks();
        List<Book> matches = books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
        if (matches.isEmpty()) {
            System.out.println("No available books found matching: " + title);
            return -1;
        }
        if (matches.size() == 1) {
            return matches.get(0).getId();
        }
        System.out.println("Multiple books found:");
        for (int i = 0; i < matches.size(); i++) {
            Book b = matches.get(i);
            System.out.println((i + 1) + ") " + b.getTitle() + " by " + b.getAuthor() + " (id=" + b.getId() + ")");
        }
        int choice = readInt("Choose book (1-" + matches.size() + "): ");
        if (choice < 1 || choice > matches.size()) {
            throw new ValidationException("Book choice out of range");
        }
        return matches.get(choice - 1).getId();
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
                controller.returnBook(lastLoanIdForReturn);
                System.out.println("Returned with fine.");
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
}
