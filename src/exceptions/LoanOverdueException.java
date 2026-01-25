package exceptions;

public class LoanOverdueException extends ConflictException {
    public LoanOverdueException(String message) {
        super(message);
    }
}
