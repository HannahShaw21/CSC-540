package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class SearchBookEditionsByAuthor extends Operation {

    public String author;

    public SearchBookEditionsByAuthor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}