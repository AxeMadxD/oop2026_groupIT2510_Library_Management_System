package domain;

public class ReferenceBook extends Book {
    public ReferenceBook(String title, String author) {
        super(title, author, false, "REFERENCE"); // reference нельзя брать домой
    }
}
