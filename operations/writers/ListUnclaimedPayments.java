package operations.writers;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class ListUnclaimedPayments extends Operation {

    public Optional<LocalDate> from = Optional.empty();
    public Optional<LocalDate> to = Optional.empty();

    public ListUnclaimedPayments() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}