package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class AddPaymentFromDistributor extends Operation {

    public String distributor;
    public float payment;

    public AddPaymentFromDistributor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}