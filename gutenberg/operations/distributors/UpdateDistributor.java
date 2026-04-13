package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.Statement;
import java.util.Optional;

public class UpdateDistributor extends Operation {

    public String name;
    public Optional<String> newName = Optional.empty();
    public Optional<String> type = Optional.empty();
    public Optional<String> contactName = Optional.empty();
    public Optional<String> phoneNum = Optional.empty();
    public Optional<String> streetAddr;
    public Optional<String> city = Optional.empty();
    public Optional<Float> totalBilled = Optional.empty();
    public Optional<Float> totalPaid = Optional.empty();

    public UpdateDistributor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}