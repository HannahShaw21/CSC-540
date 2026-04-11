package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class RecalculateDistributorBill extends Operation {

    public String distributor;

    public RecalculateDistributorBill() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}