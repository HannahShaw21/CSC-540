package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Task 2.11: Generate a report of all unclaimed payments.
 * Lists payments where 'claimDate' is NULL, with optional date range filtering.
 */
public class ListUnclaimedPayments extends Operation {

    /** Optional start date for the payment search. */
    public Optional<LocalDate> from = Optional.empty();
    
    /** Optional end date for the payment search. */
    public Optional<LocalDate> to = Optional.empty();

    public ListUnclaimedPayments() {}

    /**
     * Executes the query and prints a formatted report to the console.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database query fails.
     */
    @Override
    public void run(Statement stmt) {
        // Base query: only look for payments that haven't been claimed yet
        StringBuilder sql = new StringBuilder("SELECT * FROM Payments WHERE claimDate IS NULL");

        // Add date range filters if the user provided them
        if (from.isPresent()) {
            sql.append(" AND payDate >= \"").append(from.get().toString()).append("\"");
        }
        if (to.isPresent()) {
            sql.append(" AND payDate <= \"").append(to.get().toString()).append("\"");
        }

        sql.append(" ORDER BY payDate ASC;");

        try {
            ResultSet rs = stmt.executeQuery(sql.toString());
            
            System.out.println("\n--- Unclaimed Payments Report ---");
            System.out.printf("%-5s | %-10s | %-12s | %-10s | %-20s\n", 
                              "ID", "Writer ID", "Amount", "Pay Date", "Reason");
            System.out.println("-------------------------------------------------------------------------");

            boolean foundAny = false;
            while (rs.next()) {
                foundAny = true;
                System.out.printf("%-5d | %-10d | $%-11.2f | %-12s | %-20s\n", 
                    rs.getInt("paymentID"), 
                    rs.getInt("writerID"), 
                    rs.getFloat("amount"), 
                    rs.getString("payDate"), 
                    rs.getString("reason") == null ? "N/A" : rs.getString("reason"));
            }

            if (!foundAny) {
                System.out.println("No unclaimed payments found for the specified criteria.");
            }
            System.out.println("-------------------------------------------------------------------------\n");

        } catch (SQLException e) {
            throw new FailedOperationException("Failed to list unclaimed payments: " + e.getMessage());
        }
    }
}
