package config;

public class DbConfig {
    private static final DbSettings SETTINGS = new EnvDbSettings();
    // private static final DbSettings SETTINGS = new HardcodedDbSettings();

    private final String url;
    private final String user;
    private final String password;

    public DbConfig() {
        this.url = SETTINGS.url();
        this.user = SETTINGS.user();
        this.password = SETTINGS.password();
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
