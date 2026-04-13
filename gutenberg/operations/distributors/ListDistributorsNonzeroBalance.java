package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 3.4: List all distributors with a non-zero balance.
 * A non-zero balance indicates that 'totalBilled' does not equal 'totalPaid'.
 */
public class ListDistributorsNonzeroBalance extends Operation {

    public ListDistributorsNonzeroBalance() {}

    /**
     * Executes the query and displays the debt/credit status for each distributor.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Query targets distributors where the accounts haven't settled to zero
        String sql = "SELECT distributorName, totalBilled, totalPaid, (totalBilled - totalPaid) AS balance " +
                     "FROM Distributors " +
                     "WHERE totalBilled != totalPaid " +
                     "ORDER BY balance DESC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Distributors with Outstanding Balances ---");
            System.out.printf("%-25s | %-12s | %-12s | %-12s\n", 
                              "Distributor Name", "Total Billed", "Total Paid", "Balance Due");
            System.out.println("-----------------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                float billed = rs.getFloat("totalBilled");
                float paid = rs.getFloat("totalPaid");
                float balance = rs.getFloat("balance");

                System.out.printf("%-25s | $%-11.2f | $%-11.2f | $%-11.2f\n", 
                    rs.getString("distributorName"),
                    billed,
                    paid,
                    balance);
            }

            if (!found) {
                System.out.println("All distributor accounts are currently settled (Zero Balance).");
            }
            System.out.println("-----------------------------------------------------------------------------\n");

        } catch (SQLException e) {
            // No import needed for FailedOperationException if it's in this folder!
            throw new FailedOperationException("Failed to retrieve balance report: " + e.getMessage());
        }
    }
}
