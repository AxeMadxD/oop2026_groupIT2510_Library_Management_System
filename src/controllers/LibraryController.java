package controllers;

import domain.Book;
import domain.Category;
import domain.Loan;
import domain.Member;
import exceptions.BookNotFoundException;
import exceptions.CategoryNotFoundException;
import exceptions.MemberNotFoundException;
import repositories.BookRepository;
import repositories.CategoryRepository;
import repositories.LoanRepository;
import repositories.MemberRepository;
import services.LoanService;

import java.util.List;

public class LibraryController {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LoanService loanService;

    public LibraryController(CategoryRepository categoryRepository,
                             BookRepository bookRepository,
                             MemberRepository memberRepository,
                             LoanRepository loanRepository,
                             LoanService loanService) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
        this.loanService = loanService;
    }

    public Category addCategory(String name) {
        return categoryRepository.save(new Category(name));
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + id));
    }

    public Book addBook(int categoryId, String title, String author) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));
        return bookRepository.save(new Book(categoryId, title, author));
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

    public Member addMember(String email, String fullName) {
        return memberRepository.save(new Member(email, fullName));
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
