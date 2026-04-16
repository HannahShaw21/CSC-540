package gutenberg.operations.orders;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 1.5 (Periodicals): Place a new order for a specific periodical issue.
 * <p>
 * This operation records a transaction between a distributor and the publisher
 * for a specific issue ID, calculating the total cost including shipping.
 * </p>
 */
public class PlaceIssueOrder extends Operation {

    public String distributor;
    public int issueID;
    public float unitPrice;
    public int quantity;
    public float shippingPrice;
    public LocalDate orderDate;
    public LocalDate deliveryDate;

    public PlaceIssueOrder() {}

    /**
     * Executes the insertion of the issue order.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the issueID or Distributor name is invalid.
     */
    @Override
    public void run(Statement stmt) {
        // Constructing the SQL INSERT statement
        String sql = "INSERT INTO Orders (disName, issueID, unitPrice, quantity, shippingCost, orderDate, deliveryDate) VALUES (" +
                     "\"" + distributor + "\", " + 
                     issueID + ", " + 
                     unitPrice + ", " + 
                     quantity + ", " + 
                     shippingPrice + ", " + 
                     "\"" + orderDate.toString() + "\", " + 
                     "\"" + deliveryDate.toString() + "\");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                // Calculate total for user feedback
                float total = (unitPrice * quantity) + shippingPrice;
                System.out.println("Success: Order placed for Issue ID " + issueID + ".");
                System.out.printf("Distributor: %s | Total Charge: $%.2f\n", distributor, total);
                System.out.println("Status: Scheduled for delivery on " + deliveryDate + ".");
            }

        } catch (SQLException e) {
            // This usually hits if the distributor isn't in the Distributors table
            // or if the issueID doesn't exist in the Issues table.
            throw new FailedOperationException("Failed to place issue order. Ensure the Issue ID and Distributor name exist. Error: " + e.getMessage());
        }
    }
}
