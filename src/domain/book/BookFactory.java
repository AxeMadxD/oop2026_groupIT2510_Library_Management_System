package domain.book;

import exceptions.BookTypeNotSupportedException;

public class BookFactory {

    public enum BookType {
        PRINTED,
        EBOOK,
        REFERENCE;

        public static BookType fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                throw new BookTypeNotSupportedException("Book type is required");
            }
            try {
                return BookType.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BookTypeNotSupportedException("Unknown book type: " + value);
            }
        }
    }

    public static Book createBook(BookType type, String title, String author) {
        return switch (type) {
            case PRINTED -> new PrintedBook(title, author);
            case EBOOK -> new Ebook(title, author);
            case REFERENCE -> new ReferenceBook(title, author);
        };
    }

    public static Book createBook(String type, String title, String author) {
        return createBook(BookType.fromString(type), title, author);
    }
}
