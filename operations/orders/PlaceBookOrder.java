package operations.orders;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class PlaceBookOrder extends Operation {

    public String distributor;
    public String isbn;
    public float unitPrice;
    public int quantity;
    public float shippingPrice;
    public LocalDate orderDate;
    public LocalDate deliveryDate;

    public PlaceBookOrder() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}