package operations.writers;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class CreateNewWriter extends Operation {

    public String name;
    public String staffOrInvited;

    public CreateNewWriter() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}