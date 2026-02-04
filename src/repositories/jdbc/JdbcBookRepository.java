package repositories.jdbc;

import db.Database;
import domain.book.Book;
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
        if (book.getId() == 0) {
            String sql = "INSERT INTO books (title, author, available, type) VALUES (?, ?, ?, ?)";
            try (Connection conn = db.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, book.getTitle());
                stmt.setString(2, book.getAuthor());
                stmt.setBoolean(3, book.isAvailable());
                stmt.setString(4, book.getType());
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        book.setId(keys.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to save book", e);
            }
        } else {
            String sql = "UPDATE books SET title = ?, author = ?, available = ?, type = ? WHERE id = ?";
            try (Connection conn = db.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, book.getTitle());
                stmt.setString(2, book.getAuthor());
                stmt.setBoolean(3, book.isAvailable());
                stmt.setString(4, book.getType());
                stmt.setInt(5, book.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to update book", e);
            }
        }
        return book;
    }

    @Override
    public Optional<Book> findById(Integer id) {
        String sql = "SELECT id, title, author, available, type FROM books WHERE id = ?";
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
        String sql = "SELECT id, title, author, available, type FROM books ORDER BY id";
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
    public void deleteById(Integer id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete book", e);
        }
    }

    @Override
    public List<Book> findAvailable() {
        String sql = "SELECT id, title, author, available, type FROM books WHERE available = TRUE ORDER BY id";
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
                rs.getString("title"),
                rs.getString("author"),
                rs.getBoolean("available"),
                rs.getString("type")
        );
    }
}
