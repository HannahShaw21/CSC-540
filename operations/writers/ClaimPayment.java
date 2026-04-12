package operations.writers;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class ClaimPayment extends Operation {

    public int paymentID;
    public LocalDate claimDate;

    public ClaimPayment() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}