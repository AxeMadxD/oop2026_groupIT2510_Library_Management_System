package config;

public class EnvDbSettings implements DbSettings {
    @Override
    public String url() {
        return getEnvOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/library_db");
    }

    @Override
    public String user() {
        return getEnvOrDefault("DB_USER", "postgres");
    }

    @Override
    public String password() {
        return getEnvOrDefault("DB_PASSWORD", "");
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return value;
    }
}
