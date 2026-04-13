package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.Statement;
import java.util.Optional;

public class SearchDistributors extends Operation {

    public Optional<String> type = Optional.empty();
    public Optional<String> city = Optional.empty();

    public SearchDistributors() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}