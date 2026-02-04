package domain.book;

public class Book {
    private int id;
    private String title;
    private String author;
    private boolean available;
    private String type;


    public Book(String title, String author, boolean available, String type) {
        this.title = title;
        this.author = author;
        this.available = available;
        this.type = type;
    }

    public Book(int id, String title, String author, boolean available, String type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
        this.type = type;
    }


    public Book(int id, String title, String author, boolean available) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + available +
                ", type='" + type + '\'' +
                '}';
    }
}
