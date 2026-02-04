package domain.report;

import domain.book.Book;
import domain.loan.Loan;
import domain.member.Member;

import java.time.LocalDate;

public class LoanReport {
    private final Loan loan;
    private final Member member;
    private final Book book;
    private final Integer fine;
    private final LocalDate returnDate;

    private LoanReport(Builder builder) {
        this.loan = builder.loan;
        this.member = builder.member;
        this.book = builder.book;
        this.fine = builder.fine;
        this.returnDate = builder.returnDate;
    }

    public Loan getLoan() {
        return loan;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public Integer getFine() {
        return fine;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public static class Builder {
        private Loan loan;
        private Member member;
        private Book book;
        private Integer fine;
        private LocalDate returnDate;

        public Builder() {}

        public Builder loan(Loan loan) { this.loan = loan; return this; }
        public Builder member(Member member) { this.member = member; return this; }
        public Builder book(Book book) { this.book = book; return this; }
        public Builder fine(Integer fine) { this.fine = fine; return this; }
        public Builder returnDate(LocalDate returnDate) { this.returnDate = returnDate; return this; }

        public LoanReport build() {
            return new LoanReport(this);
        }
    }
}
