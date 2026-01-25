package domain;

public class Member {
    private int id;
    private String email;
    private String fullName;

    public Member() {
    }

    public Member(int id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    public Member(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Member{id=" + id + ", email='" + email + "', fullName='" + fullName + "'}";
    }
}
