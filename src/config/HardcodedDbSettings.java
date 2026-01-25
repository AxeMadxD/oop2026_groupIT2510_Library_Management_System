package config;

public class HardcodedDbSettings implements DbSettings {
    private static final String URL =
        "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER =
        "postgres.iglfxlejcrvyjniotnxa";
    private static final String PASSWORD =
        "";

    @Override public String url() { return URL; }
    @Override public String user() { return USER; }
    @Override public String password() { return PASSWORD; }
}
