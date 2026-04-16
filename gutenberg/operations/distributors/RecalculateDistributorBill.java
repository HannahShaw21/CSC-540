package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 3.2: Recalculate the total billed amount for a specific distributor.
 * <p>
 * This operation scans the 'Orders' table for all records matching the distributor
 * and updates the 'totalBilled' field in the 'Distributors' table to ensure
 * financial data consistency.
 * </p>
 */
public class RecalculateDistributorBill extends Operation {

    /** The name of the distributor to recalculate. */
    public String distributor;

    public RecalculateDistributorBill() {}

    /**
     * Executes the recalculation logic using a two-step aggregate query.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the calculation or update fails.
     */
    @Override
    public void run(Statement stmt) {
        // Step 1: Sum up the cost of all orders for this distributor.
        String calculateSql = "SELECT SUM(unitPrice * quantity) AS totalSum FROM Orders " +
                              "WHERE disName = \"" + distributor + "\";";

        try {
            ResultSet rs = stmt.executeQuery(calculateSql);
            float updatedTotal = 0.0f;

            if (rs.next()) {
                updatedTotal = rs.getFloat("totalSum");
            }

            // Step 2: Update the master record in the Distributors table.
            String updateSql = "UPDATE Distributors SET totalBilled = " + updatedTotal + " " +
                               "WHERE name = \"" + distributor + "\";";

            int rowsAffected = stmt.executeUpdate(updateSql);

            if (rowsAffected > 0) {
                System.out.printf("Success: Recalculated bill for '%s'. New 'Total Billed' is $%.2f\n",
                                  distributor, updatedTotal);
            } else {
                System.out.println("Notice: No distributor found with name '" + distributor + "'. No changes made.");
            }

        } catch (SQLException e) {
            // Standardizing error reporting for the CLI
            throw new FailedOperationException("Failed to recalculate distributor bill: " + e.getMessage());
        }
    }
}
