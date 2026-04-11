package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.util.Optional;

public class DeleteDistributor extends Operation {

    public String name;

    public DeleteDistributor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}