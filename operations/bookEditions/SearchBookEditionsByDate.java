package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class SearchBookEditionsByDate extends Operation {

    public LocalDate from;
    public LocalDate to;

    public SearchBookEditionsByDate() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}