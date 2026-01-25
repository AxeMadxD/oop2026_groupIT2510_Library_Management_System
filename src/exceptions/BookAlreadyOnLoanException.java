package exceptions;

public class BookAlreadyOnLoanException extends ConflictException {
    public BookAlreadyOnLoanException(String message) {
        super(message);
    }
}
