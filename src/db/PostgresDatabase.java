package db;

import config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDatabase implements Database {
    private final DbConfig config;

    public PostgresDatabase() {
        this.config = new DbConfig();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUser(),
                config.getPassword()
        );
    }
}
