package controllers;

import components.catalog.CatalogComponent;
import components.loan.LoanManagementComponent;
import components.member.MemberManagementComponent;
import components.reporting.ReportingComponent;
import domain.book.Book;
import domain.loan.Loan;
import domain.member.Member;
import domain.report.LoanReport;

import java.util.List;
import java.util.Optional;

public class LibraryController {
    private final CatalogComponent catalogComponent;
    private final MemberManagementComponent memberManagementComponent;
    private final LoanManagementComponent loanManagementComponent;
    private final ReportingComponent reportingComponent;

    public LibraryController(CatalogComponent catalogComponent,
                             MemberManagementComponent memberManagementComponent,
                             LoanManagementComponent loanManagementComponent,
                             ReportingComponent reportingComponent) {
        this.catalogComponent = catalogComponent;
        this.memberManagementComponent = memberManagementComponent;
        this.loanManagementComponent = loanManagementComponent;
        this.reportingComponent = reportingComponent;
    }

    public Book addBook(String type, String title, String author) {
        return catalogComponent.addBook(type, title, author);
    }

    public List<Book> listAvailableBooks() {
        return catalogComponent.listAvailableBooks();
    }

    public List<Book> listAllBooks() {
        return catalogComponent.listAllBooks();
    }

    public Book findBookById(int id) {
        return catalogComponent.findBookById((long) id).orElse(null);
    }

    public Member addMember(String fullName) {
        return memberManagementComponent.addMember(fullName);
    }

    public List<Member> listMembers() {
        return memberManagementComponent.listMembers();
    }

    public Member findMemberById(int id) {
        return memberManagementComponent.findMemberById((long) id).orElse(null);
    }

    public Loan borrowBook(int memberId, int bookId) {
        return loanManagementComponent.borrowBook(memberId, bookId);
    }

    public void returnBook(int loanId) {
        loanManagementComponent.returnBook(loanId);
    }

    public List<Loan> viewCurrentLoansPerMember(int memberId) {
        return loanManagementComponent.getLoansByMember(memberId);
    }

    public Optional<LoanReport> buildLoanReport(long memberId) {
        return reportingComponent.buildLoanReport(memberId);
    }
}
