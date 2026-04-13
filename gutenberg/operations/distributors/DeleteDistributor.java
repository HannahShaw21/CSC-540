package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Delete a distributor record from the system.
 * <p>
 * This operation removes a distributor identified by their unique name.
 * Note: This operation may fail if the distributor is referenced by 
 * existing orders or financial records (Foreign Key constraints).
 * </p>
 */
public class DeleteDistributor extends Operation {

    /** * The unique name of the distributor to be deleted.
     * Matches the 'distributorName' column in the database.
     */
    public String name;

    /**
     * Default constructor for the DeleteDistributor operation.
     */
    public DeleteDistributor() {}

    /**
     * Executes the SQL DELETE command.
     * * @param stmt The active JDBC Statement used to communicate with MariaDB.
     * @throws FailedOperationException If the distributor name is not found, 
     * or if a database integrity constraint (like a Foreign Key) 
     * prevents the deletion.
     */
    @Override
    public void run(Statement stmt) {
        // Double-quote standard applied for SQL safety
        String sql = "DELETE FROM Distributors WHERE distributorName = \"" + name + "\";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Distributor '" + name + "' has been removed.");
            } else {
                System.out.println("Notice: No distributor found with the name '" + name + "'.");
            }
        } catch (SQLException e) {
            // Explaining the error to the user (likely a Foreign Key issue)
            throw new FailedOperationException("Could not delete distributor. Check if they have linked orders. Error: " + e.getMessage());
        }
    }
}
