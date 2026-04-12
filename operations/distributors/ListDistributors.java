package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.util.Optional;

public class ListDistributors extends Operation {

    public ListDistributors() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}