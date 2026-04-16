package gutenberg.operations.orders;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 1.5: Place a new order for a book edition.
 * <p>
 * This operation records a transaction between a distributor and the publisher.
 * It identifies the book by ISBN and calculates the total cost based on 
 * unit price, quantity, and shipping.
 * </p>
 */
public class PlaceBookOrder extends Operation {

    public String distributor;
    public String isbn;
    public float unitPrice;
    public int quantity;
    public float shippingPrice;
    public LocalDate orderDate;
    public LocalDate deliveryDate;

    public PlaceBookOrder() {}

    /**
     * Executes the order placement.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the ISBN or Distributor name is invalid.
     */
    @Override
    public void run(Statement stmt) {
        // Note: Dates and Strings are quoted; numeric types (float/int) are not.
        String sql = "INSERT INTO Orders (disName, isbn, unitPrice, quantity, shippingCost, orderDate, deliveryDate) VALUES (" +
                     "\"" + distributor + "\", " + 
                     "\"" + isbn + "\", " + 
                     unitPrice + ", " + 
                     quantity + ", " + 
                     shippingPrice + ", " + 
                     "\"" + orderDate.toString() + "\", " + 
                     "\"" + deliveryDate.toString() + "\");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                float total = (unitPrice * quantity) + shippingPrice;
                System.out.println("Success: Order placed for ISBN " + isbn + ".");
                System.out.printf("Distributor: %s | Total Charge: $%.2f\n", distributor, total);
                System.out.println("Expected Delivery: " + deliveryDate);
            }

        } catch (SQLException e) {
            // This will trigger if the ISBN doesn't exist (Foreign Key violation)
            // or if the Distributor name doesn't exist.
            throw new FailedOperationException("Failed to place order. Check ISBN and Distributor name. Error: " + e.getMessage());
        }
    }
}
