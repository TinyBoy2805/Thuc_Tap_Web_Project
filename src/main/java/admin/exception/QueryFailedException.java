package admin.exception;

public class QueryFailedException extends RuntimeException {
    public QueryFailedException(String s) {
        super(s);
    }
}
