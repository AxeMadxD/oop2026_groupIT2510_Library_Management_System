package domain;

public class Book {
    private int id;
    private int categoryId;
    private String title;
    private String author;
    private boolean available;

    public Book() {
    }

    public Book(int id, int categoryId, String title, String author, boolean available) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public Book(int categoryId, String title, String author) {
        this(0, categoryId, title, author, true);
    }

    public Book(int categoryId, String title, String author, boolean available) {
        this.categoryId = categoryId;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", categoryId=" + categoryId + ", title='" + title
                + "', author='" + author + "', available=" + available + "}";
    }
}
