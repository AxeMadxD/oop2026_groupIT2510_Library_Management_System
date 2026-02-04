package repositories;

import domain.loan.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends CrudRepository<Loan, Integer> {
    Optional<Loan> findActiveById(int loanId);

    Optional<Loan> findActiveByBookId(int bookId);

    List<Loan> findCurrentLoansByMemberId(int memberId);

    void markReturned(int loanId, LocalDate returnDate);
}
