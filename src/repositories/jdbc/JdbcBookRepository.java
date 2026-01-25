package repositories.jdbc;

import db.Database;
import domain.Book;
import exceptions.DatabaseOperationException;
import repositories.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookRepository implements BookRepository {
    private final Database db;

    public JdbcBookRepository(Database db) {
        this.db = db;
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (category_id, title, author, available) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, book.getCategoryId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setBoolean(4, book.isAvailable());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setId(keys.getInt(1));
                }
            }
            return book;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save book", e);
        }
    }

    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT id, category_id, title, author, available FROM books WHERE id = ?";
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
            throw new DatabaseOperationException("Failed to find book by id", e);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, category_id, title, author, available FROM books ORDER BY id";
        List<Book> books = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to list books", e);
        }
    }

    @Override
    public List<Book> findAvailable() {
        String sql = "SELECT id, category_id, title, author, available FROM books WHERE available = TRUE ORDER BY id";
        List<Book> books = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to list available books", e);
        }
    }

    @Override
    public void updateAvailability(int bookId, boolean available) {
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update book availability", e);
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getInt("category_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getBoolean("available")
        );
    }
}
