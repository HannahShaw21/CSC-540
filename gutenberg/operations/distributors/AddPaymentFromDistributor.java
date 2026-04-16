package gutenberg.operations.distributors;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.9 (Distributors): Record a payment received from a distributor.
 * Adds a new entry to the financial records for incoming revenue.
 */
public class AddPaymentFromDistributor extends Operation {

    /** The name of the distributor making the payment. */
    public String distributor;
    
    /** The amount paid. */
    public float payment;

    public AddPaymentFromDistributor() {}

    /**
     * Executes the insertion of the distributor payment.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the distributor doesn't exist 
     * or a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        String sql = String.format("UPDATE Distributor SET totalPaid = totalPaid + %f WHERE name = %s;",
                payment,
                Util.sqlStrWrapper(distributor)
        );

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Recorded a payment of $" + payment + " from '" + distributor + "'.");
            } else {
                System.out.println("Notice: No payment was recorded. Please check the distributor name.");
            }
        } catch (SQLException e) {
            // Standard error handling to pass info back to the CLI
            throw new FailedOperationException("Failed to add distributor payment: " + e.getMessage());
        }
    }
}
