package repositories.jdbc;

import db.Database;
import domain.loan.Loan;
import exceptions.DatabaseOperationException;
import repositories.LoanRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcLoanRepository implements LoanRepository {
    private final Database db;

    public JdbcLoanRepository(Database db) {
        this.db = db;
    }

    @Override
    public Loan save(Loan loan) {
        if (loan.getId() == 0) {
            // Insert new loan
            String sql = "INSERT INTO loans (book_id, member_id, loan_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = db.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, loan.getBookId());
                stmt.setInt(2, loan.getMemberId());
                stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
                stmt.setDate(4, Date.valueOf(loan.getDueDate()));
                if (loan.getReturnDate() == null) {
                    stmt.setNull(5, java.sql.Types.DATE);
                } else {
                    stmt.setDate(5, Date.valueOf(loan.getReturnDate()));
                }
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        loan.setId(keys.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to save loan", e);
            }
        } else {
            // Update existing loan
            String sql = "UPDATE loans SET book_id = ?, member_id = ?, loan_date = ?, due_date = ?, return_date = ? WHERE id = ?";
            try (Connection conn = db.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, loan.getBookId());
                stmt.setInt(2, loan.getMemberId());
                stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
                stmt.setDate(4, Date.valueOf(loan.getDueDate()));
                if (loan.getReturnDate() == null) {
                    stmt.setNull(5, java.sql.Types.DATE);
                } else {
                    stmt.setDate(5, Date.valueOf(loan.getReturnDate()));
                }
                stmt.setInt(6, loan.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to update loan", e);
            }
        }
        return loan;
    }

    @Override
    public Optional<Loan> findById(Integer id) {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loans WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find loan by id", e);
        }
    }

    @Override
    public List<Loan> findAll() {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loans ORDER BY id";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                loans.add(mapRow(rs));
            }
            return loans;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to list loans", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete loan", e);
        }
    }

    // ------------------- Loan-specific Methods -------------------

    @Override
    public Optional<Loan> findActiveByBookId(int bookId) {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loans WHERE book_id = ? AND return_date IS NULL";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find active loan by book id", e);
        }
    }

    @Override
    public Optional<Loan> findActiveById(int loanId) {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loans WHERE id = ? AND return_date IS NULL";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find active loan by id", e);
        }
    }

    @Override
    public List<Loan> findCurrentLoansByMemberId(int memberId) {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loans WHERE member_id = ? AND return_date IS NULL ORDER BY id";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapRow(rs));
                }
            }
            return loans;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find current loans by member", e);
        }
    }

    @Override
    public void markReturned(int loanId, LocalDate returnDate) {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setInt(2, loanId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to mark loan returned", e);
        }
    }


    private Loan mapRow(ResultSet rs) throws SQLException {
        LocalDate returnDate = null;
        Date returnSqlDate = rs.getDate("return_date");
        if (returnSqlDate != null) {
            returnDate = returnSqlDate.toLocalDate();
        }
        return new Loan(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate
        );
    }
}
