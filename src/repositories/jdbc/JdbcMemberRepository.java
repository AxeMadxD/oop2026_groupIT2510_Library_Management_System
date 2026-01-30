package repositories.jdbc;

import db.Database;
import domain.Member;
import exceptions.DatabaseOperationException;
import repositories.MemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
    private final Database db;

    public JdbcMemberRepository(Database db) {
        this.db = db;
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO members (full_name) VALUES (?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, member.getFullName());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    member.setId(keys.getInt(1));
                }
            }
            return member;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save member", e);
        }
    }


    @Override
    public Optional<Member> findById(int id) {
        String sql = "SELECT id, full_name FROM members WHERE id = ?";
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
            throw new DatabaseOperationException("Failed to find member by id", e);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, full_name FROM members ORDER BY id";
        List<Member> members = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                members.add(mapRow(rs));
            }
            return members;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to list members", e);
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("id"),
                rs.getString("full_name")
        );
    }
}
