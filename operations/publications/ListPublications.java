package operations.publications;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class ListPublications extends Operation {

    public ListPublications() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}