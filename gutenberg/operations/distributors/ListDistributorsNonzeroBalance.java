package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.Statement;

public class ListDistributorsNonzeroBalance extends Operation {

    public ListDistributorsNonzeroBalance() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}