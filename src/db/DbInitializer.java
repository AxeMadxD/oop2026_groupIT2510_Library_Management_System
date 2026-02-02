package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {
    public static void init(Database db) {
        try (Connection conn = db.getConnection(); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS books (" +
                            "id SERIAL PRIMARY KEY, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "author VARCHAR(255) NOT NULL, " +
                            "available BOOLEAN NOT NULL DEFAULT TRUE, " +
                            "type VARCHAR(20) NOT NULL" +
                            ")"
            );


            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS members (" +
                        "id SERIAL PRIMARY KEY, " +
                    "full_name VARCHAR(255) NOT NULL" +
                    ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS loans (" +
                    "id SERIAL PRIMARY KEY, " +
                    "book_id INT NOT NULL REFERENCES books(id), " +
                    "member_id INT NOT NULL REFERENCES members(id), " +
                    "loan_date DATE NOT NULL, " +
                    "due_date DATE NOT NULL, " +
                    "return_date DATE NULL" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
