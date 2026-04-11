package operations.orders;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class PlaceIssueOrder extends Operation {

    public String distributor;
    public int issueID;
    public float unitPrice;
    public int quantity;
    public float shippingPrice;
    public LocalDate orderDate;
    public LocalDate deliveryDate;

    public PlaceIssueOrder() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}