package operations.publications;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of the AssignEditorToPublication operation.
 * Links a writer (editor) to a publication in the PublicationEditedBy table.
 */
public class AssignEditorToPublication extends Operation {

    public int writerID;
    public int pubID;

    /**
     * Default constructor required for reflection.
     */
    public AssignEditorToPublication() {}

    /**
     * Executes the SQL INSERT command to link an editor to a publication.
     * @param stmt The active Statement provided by the GutenbergConnection.
     * @throws FailedOperationException if the IDs don't exist or the link already exists.
     */
    @Override
    public void run(Statement stmt) {
        // SQL command to insert into the junction table
        String sql = "INSERT INTO Edits (writerID, pubID) VALUES ("
                     + writerID + ", " + pubID + ");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Writer " + writerID + " is now an editor for Publication " + pubID + ".");
            }
        } catch (SQLException e) {
            // And if the writerID or pubID doesn't exist, the database will throw an error.
            throw new FailedOperationException("Failed to assign editor. Ensure both IDs are valid: " + e.getMessage());
        }
    }
}
