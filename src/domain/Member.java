package domain;

public class Member {
    private int id;
    private String fullName;

    public Member() {
    }

    public Member(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Member(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Member{id=" + id + "', fullName='" + fullName + "'}";
    }
}
