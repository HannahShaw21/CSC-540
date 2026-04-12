package operations.publications;

import operations.*;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of the AssignEditorToPublication operation.
 * Matches the 'Edits' table schema defined in Project Report #2.
 */
public class AssignEditorToPublication extends Operation {

    // Matches the column names in your SQL schema (Report 2)
    public int writerID;
    public int pubID;

    public AssignEditorToPublication() {}

    @Override
    public void run(Statement stmt) {
        // Updated to use the table name 'Edits' from Report #2
        String sql = "INSERT INTO Edits (writerID, pubID) VALUES (" 
                     + writerID + ", " + pubID + ");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("Success: Writer " + writerID + " is now an editor for Publication " + pubID + ".");
            }
        } catch (SQLException e) {
            // This will catch foreign key errors if the writerID or pubID doesn't exist
            throw new FailedOperationException("SQL Error in Edits assignment: " + e.getMessage());
        }
    }
}
