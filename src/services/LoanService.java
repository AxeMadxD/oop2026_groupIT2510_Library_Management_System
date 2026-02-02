package services;

import domain.Book;
import domain.Loan;
import exceptions.BookAlreadyOnLoanException;
import exceptions.BookNotFoundException;
import exceptions.LoanNotFoundException;
import exceptions.LoanOverdueException;
import exceptions.MemberNotFoundException;
import repositories.BookRepository;
import repositories.LoanRepository;
import repositories.MemberRepository;

import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final FineCalculator fineCalculator;

    public LoanService(BookRepository bookRepository,
                       MemberRepository memberRepository,
                       LoanRepository loanRepository,
                       FineCalculator fineCalculator) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
        this.fineCalculator = fineCalculator;
    }

    public Loan borrowBook(int memberId, int bookId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + bookId));

        if (!book.isAvailable()) {
            throw new BookAlreadyOnLoanException("Book is already on loan: " + bookId);
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(14);
        Loan loan = new Loan(book.getId(), memberId, today, dueDate, null);

        // <-- Use save() instead of create()
        Loan created = loanRepository.save(loan);

        bookRepository.updateAvailability(book.getId(), false);
        return created;
    }


    public int returnBook(int loanId) {
        Loan loan = loanRepository.findActiveById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Active loan not found: " + loanId));

        LocalDate today = LocalDate.now();
        if (today.isAfter(loan.getDueDate())) {
            throw new LoanOverdueException("Loan is overdue. Due date was " + loan.getDueDate());
        }

        loanRepository.markReturned(loanId, today);
        bookRepository.updateAvailability(loan.getBookId(), true);
        return fineCalculator.calculateFine(loan.getDueDate(), today);
    }

    public int forceReturnOverdue(int loanId) {
        Loan loan = loanRepository.findActiveById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Active loan not found: " + loanId));

        LocalDate today = LocalDate.now();
        loanRepository.markReturned(loanId, today);
        bookRepository.updateAvailability(loan.getBookId(), true);
        return fineCalculator.calculateFine(loan.getDueDate(), today);
    }

    public List<Loan> getCurrentLoansForMember(int memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
        return loanRepository.findCurrentLoansByMemberId(memberId);
    }
}
