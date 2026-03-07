package exception;

public class InvalidUserIdException extends IllegalArgumentException {
    public InvalidUserIdException(String s) {
        super(s);
    }
}
