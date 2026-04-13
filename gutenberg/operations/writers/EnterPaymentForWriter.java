package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Task 2.9: Record a new payment for a writer.
 * This operation adds a record to the Payments table. The payment 
 * remains 'unclaimed' until the claimDate is updated.
 */
public class EnterPaymentForWriter extends Operation {

    /** The ID of the writer receiving the payment. */
    public int writerID;
    
    /** The monetary value of the payment. */
    public float amount;
    
    /** The date the payment was issued/authorized. */
    public LocalDate payDate;
    
    /** Optional description or reason for the payment (e.g., "Article Bonus"). */
    public Optional<String> reason = Optional.empty();

    public EnterPaymentForWriter() {}

    /**
     * Executes the insertion of the payment record.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the writerID does not exist 
     * or a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // If the reason is present, wrap it in double quotes; otherwise, use NULL.
        String reasonVal = reason.isPresent() ? "\"" + reason.get() + "\"" : "NULL";

        // Construct the SQL using the established double-quote standard for strings and dates.
        String sql = "INSERT INTO Payments (writerID, amount, payDate, reason) VALUES (" +
                     writerID + ", " + 
                     amount + ", " + 
                     "\"" + payDate.toString() + "\", " + 
                     reasonVal + ");";

        try {
            stmt.executeUpdate(sql);
            System.out.println("Success: Payment of $" + amount + " has been recorded for Writer ID: " + writerID + ".");
        } catch (SQLException e) {
            // Pass the database error back to the CLI layer.
            throw new FailedOperationException("Failed to enter writer payment: " + e.getMessage());
        }
    }
}
