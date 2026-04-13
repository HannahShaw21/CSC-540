package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.8 (General): List all distribution partners and their current status.
 * Displays contact information and financial balances in a formatted table.
 */
public class ListDistributors extends Operation {

    public ListDistributors() {}

    /**
     * Executes the query and prints a formatted report.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the column names do not match the schema.
     */
    @Override
    public void run(Statement stmt) {
        // FIXED: Using 'Distributors' and ordering by name for a better report
        String sql = "SELECT * FROM Distributors ORDER BY distributorName ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Distributor Directory ---");
            // Header for the table
            System.out.printf("%-20s | %-10s | %-15s | %-12s | %-12s | %-10s\n", 
                              "Name", "Type", "Contact", "Phone", "Billed", "Paid");
            System.out.println("-------------------------------------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                // FIXED: Column names matched to your CreateNewDistributor logic
                System.out.printf("%-20s | %-10s | %-15s | %-12s | $%-11.2f | $%-9.2f\n", 
                    rs.getString("distributorName"),
                    rs.getString("type"),
                    rs.getString("contactPerson") == null ? "N/A" : rs.getString("contactPerson"),
                    rs.getString("phoneNum") == null ? "N/A" : rs.getString("phoneNum"),
                    rs.getFloat("totalBilled"),
                    rs.getFloat("totalPaid"));
            }

            if (!found) {
                System.out.println("No distributors found in the system.");
            }
            System.out.println("-------------------------------------------------------------------------------------------------\n");

        } catch (SQLException e) {
            // Providing a more descriptive error for debugging
            throw new FailedOperationException("Failed to retrieve distributor list. Error: " + e.getMessage());
        }
    }
}
