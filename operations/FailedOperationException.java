package operations;

public class FailedOperationException extends RuntimeException {
    public FailedOperationException(String msg) {
        super(msg);
    }
}
