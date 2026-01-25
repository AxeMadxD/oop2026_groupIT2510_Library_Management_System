package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {
    public static void init(Database db) {
        try (Connection conn = db.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS categories (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) UNIQUE NOT NULL" +
                    ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS books (" +
                    "id SERIAL PRIMARY KEY, " +
                    "category_id INT NOT NULL REFERENCES categories(id), " +
                    "title VARCHAR(255) NOT NULL, " +
                    "author VARCHAR(255) NOT NULL, " +
                    "available BOOLEAN NOT NULL DEFAULT TRUE" +
                    ")");
            try {
                stmt.executeUpdate("ALTER TABLE books DROP CONSTRAINT IF EXISTS books_isbn_key");
                stmt.executeUpdate("ALTER TABLE books DROP COLUMN IF EXISTS isbn");
            } catch (SQLException e) {
                System.out.println("Warning: unable to drop isbn column. Please adjust schema manually.");
            }

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS members (" +
                    "id SERIAL PRIMARY KEY, " +
                    "email VARCHAR(255) UNIQUE NOT NULL, " +
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
