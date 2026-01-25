package repositories.jdbc;

import db.Database;
import domain.Category;
import exceptions.DatabaseOperationException;
import repositories.CategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCategoryRepository implements CategoryRepository {
    private final Database db;

    public JdbcCategoryRepository(Database db) {
        this.db = db;
    }

    @Override
    public Category save(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
                }
            }
            return category;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save category", e);
        }
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
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
            throw new DatabaseOperationException("Failed to find category by id", e);
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, name FROM categories ORDER BY id";
        List<Category> categories = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(mapRow(rs));
            }
            return categories;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to list categories", e);
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("id"), rs.getString("name"));
    }
}
