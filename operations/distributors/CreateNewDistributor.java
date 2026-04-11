package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.util.Optional;

public class CreateNewDistributor extends Operation {

    public String name;
    public String type;
    public Optional<String> contactName = Optional.empty();
    public Optional<String> phoneNum = Optional.empty();
    public String streetAddr;
    public String city;
    public Optional<Float> totalBilled = Optional.empty();
    public Optional<Float> totalPaid = Optional.empty();

    public CreateNewDistributor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}