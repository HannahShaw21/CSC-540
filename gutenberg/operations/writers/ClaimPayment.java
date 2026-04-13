package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 2.10: Record that a writer has claimed their payment.
 * Updates the 'claimDate' in the Payments table for a specific payment ID.
 */
public class ClaimPayment extends Operation {

    /** The unique ID of the payment record. */
    public int paymentID;
    
    /** The date the payment was officially claimed by the writer. */
    public LocalDate claimDate;

    public ClaimPayment() {}

    /**
     * Executes the update to mark a payment as claimed.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the payment record cannot be found or updated.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "UPDATE Payments SET claimDate = \"" + claimDate.toString() + "\" " +
                     "WHERE paymentID = " + paymentID + ";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Payment record " + paymentID + " updated. Claim date set to " + claimDate + ".");
            } else {
                // This happens if the user enters an ID that doesn't exist in the Payments table.
                System.out.println("Notice: No payment record found with ID " + paymentID + ". No changes made.");
            }
        } catch (SQLException e) {
            // Pass the error to the CLI using the team's custom exception pattern.
            throw new FailedOperationException("Failed to record claimed payment: " + e.getMessage());
        }
    }
}
