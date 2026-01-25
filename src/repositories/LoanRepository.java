package repositories;

import domain.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan create(Loan loan);

    Optional<Loan> findActiveByBookId(int bookId);

    Optional<Loan> findActiveById(int loanId);

    List<Loan> findCurrentLoansByMemberId(int memberId);

    void markReturned(int loanId, LocalDate returnDate);
}
