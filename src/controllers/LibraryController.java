package controllers;

import domain.Book;
import domain.Loan;
import domain.Member;
import exceptions.BookNotFoundException;
import exceptions.MemberNotFoundException;
import repositories.BookRepository;
import repositories.LoanRepository;
import repositories.MemberRepository;
import services.LoanService;

import java.util.List;

public class LibraryController {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LoanService loanService;

    public LibraryController(BookRepository bookRepository,
                             MemberRepository memberRepository,
                             LoanRepository loanRepository,
                             LoanService loanService) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
        this.loanService = loanService;
    }

    public Book addBook(String title, String author) {
        Book book = new Book(title, author, true);
        return bookRepository.save(book);
    }

    public List<Book> listAvailableBooks() {
        return bookRepository.findAvailable();
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
    }

    public Member addMember(String fullName) {
        return memberRepository.save(new Member(fullName));
    }

    public List<Member> listMembers() {
        return memberRepository.findAll();
    }

    public Member findMemberById(int id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + id));
    }

    public Loan borrowBook(int memberId, int bookId) {
        return loanService.borrowBook(memberId, bookId);
    }

    public int returnBook(int loanId) {
        return loanService.returnBook(loanId);
    }

    public int forceReturnOverdue(int loanId) {
        return loanService.forceReturnOverdue(loanId);
    }

    public List<Loan> viewCurrentLoansPerMember(int memberId) {
        return loanService.getCurrentLoansForMember(memberId);
    }
}
