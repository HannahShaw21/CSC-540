package gutenberg.operations;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Remove a publication record from the database.
 * <p>
 * This operation deletes a base publication entry. Note that if foreign 
 * key constraints are active without CASCADE, this will fail if the 
 * publication has linked editions or issues.
 * </p>
 */
public class DeletePublication extends Operation {

    /** The unique ID of the publication to be removed. */
    public int pubID;

    public DeletePublication() {}

    /**
     * Executes the SQL DELETE command.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the ID is missing or a database constraint prevents deletion.
     */
    @Override
    public void run(Statement stmt) {
        // Since pubID is an integer, no quotes are required.
        String sql = "DELETE FROM Publications WHERE pubID = " + pubID + ";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Publication " + pubID + " and its metadata have been removed.");
            } else {
                // Using the team's custom exception for missing records
                throw new FailedOperationException("No publication found with ID " + pubID + ". Nothing was deleted.");
            }
        } catch (SQLException e) {
            // This usually catches Foreign Key violations
            throw new FailedOperationException("Could not delete publication: " + e.getMessage());
        }
    }
}
