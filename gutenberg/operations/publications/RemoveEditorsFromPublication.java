package gutenberg.operations;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Remove a specific editor assignment from a publication.
 * <p>
 * This operation removes the relationship between a writer and a publication
 * in the 'Edits' table without deleting the writer or publication themselves.
 * </p>
 */
public class RemoveEditorsFromPublication extends Operation {

    /** The ID of the writer to be removed from the editorial team. */
    public int writerID;

    /** The ID of the publication they are being removed from. */
    public int pubID;

    public RemoveEditorsFromPublication() {}

    /**
     * Executes the SQL DELETE command on the 'Edits' table.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database operation fails.
     */
    @Override
    public void run(Statement stmt) {
        // SQL targets the link table based on the composite key (writerID, pubID)
        String sql = "DELETE FROM Edits WHERE writerID = " + writerID + 
                     " AND pubID = " + pubID + ";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Writer ID " + writerID + 
                                   " is no longer assigned as an editor for Publication ID " + pubID + ".");
            } else {
                // If 0 rows are affected, the relationship didn't exist in the table
                System.out.println("Notice: No assignment found for Writer ID " + writerID + 
                                   " on Publication ID " + pubID + ". Nothing changed.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to remove editor assignment: " + e.getMessage());
        }
    }
}
