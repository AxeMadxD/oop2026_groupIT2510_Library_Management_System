package factories;

import domain.Book;
import domain.Ebook;
import domain.PrintedBook;
import domain.ReferenceBook;

public class BookFactory {

    public static Book createBook(String type, String title, String author) {
        return switch (type.toUpperCase()) {
            case "PRINTED" -> new PrintedBook(title, author);
            case "EBOOK" -> new Ebook(title, author);
            case "REFERENCE" -> new ReferenceBook(title, author);
            default -> throw new IllegalArgumentException("Unknown book type: " + type);
        };
    }
}

