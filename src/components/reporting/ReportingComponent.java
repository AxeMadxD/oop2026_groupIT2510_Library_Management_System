package components.reporting;

import domain.book.Book;
import domain.loan.Loan;
import domain.member.Member;
import domain.report.LoanReport;
import repositories.BookRepository;
import repositories.LoanRepository;
import repositories.MemberRepository;
import services.singleton.FinePolicy;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReportingComponent {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final FinePolicy finePolicy;

    public ReportingComponent(MemberRepository memberRepository,
                              BookRepository bookRepository,
                              LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.finePolicy = FinePolicy.getInstance();
    }

    public Optional<LoanReport> buildLoanReport(long memberId) {
        int id = Math.toIntExact(memberId);
        Member member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            return Optional.empty();
        }

        List<Loan> loans = loanRepository.findCurrentLoansByMemberId(id);
        if (loans.isEmpty()) {
            return Optional.empty();
        }

        Loan loan = loans.stream().max(Comparator.comparingInt(Loan::getId)).orElse(null);
        if (loan == null) {
            return Optional.empty();
        }

        Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());
        if (bookOpt.isEmpty()) {
            return Optional.empty();
        }
        Book book = bookOpt.get();

        LocalDate today = LocalDate.now();
        int fine = finePolicy.calculateFine(loan.getDueDate(), today);

        return Optional.of(new LoanReport.Builder()
                .loan(loan)
                .member(member)
                .book(book)
                .fine(fine)
                .returnDate(loan.getReturnDate())
                .build());
    }
}
