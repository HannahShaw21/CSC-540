package operations;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of the DeletePublication operation.
 * Removes a publication record from the database based on its unique ID.
 */
public class DeletePublication extends Operation {

    /** The ID of the publication to be deleted. */
    public int pubID;

    /**
     * Default constructor required for reflection.
     */
    public DeletePublication() {}

    /**
     * Executes the SQL DELETE command.
     * @param stmt The active Statement provided by the GutenbergConnection.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Build the SQL string based on Tyler's style
        String sql = "DELETE FROM Publications WHERE pubID = " + pubID + ";";

        try {
            // executeUpdate returns the number of rows affected
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Publication " + pubID + " has been deleted.");
            } else {
                System.out.println("Notice: No publication was found with ID " + pubID + ".");
            }
        } catch (SQLException e) {
            // Throw Tyler's custom exception if the database complains
            throw new FailedOperationException("Failed to delete publication: " + e.getMessage());
        }
    }
}
