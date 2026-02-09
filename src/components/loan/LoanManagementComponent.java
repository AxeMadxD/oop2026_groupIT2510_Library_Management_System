package components.loan;

import domain.loan.Loan;
import repositories.LoanRepository;
import services.LoanService;

import java.util.List;

public class LoanManagementComponent {
    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public LoanManagementComponent(LoanService loanService, LoanRepository loanRepository) {
        this.loanService = loanService;
        this.loanRepository = loanRepository;
    }

    public Loan borrowBook(long memberId, long bookId) {
        return loanService.borrowBook(Math.toIntExact(memberId), Math.toIntExact(bookId));
    }

    public void returnBook(long loanId) {
        loanService.returnBook(Math.toIntExact(loanId));
    }

    public List<Loan> getLoansByMember(long memberId) {
        return loanRepository.findCurrentLoansByMemberId(Math.toIntExact(memberId));
    }
}
