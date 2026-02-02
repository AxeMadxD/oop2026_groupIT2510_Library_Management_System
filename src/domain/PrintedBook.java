package domain;

public class PrintedBook extends Book {
    public PrintedBook(String title, String author) {
        super(title, author, true, "PRINTED");
    }
}

