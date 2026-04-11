package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class DeleteBookEdition extends Operation {

    public String isbn;

    public DeleteBookEdition() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}