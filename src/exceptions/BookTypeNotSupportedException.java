package exceptions;

public class BookTypeNotSupportedException extends RuntimeException {
    public BookTypeNotSupportedException(String message) {
        super(message);
    }
}
