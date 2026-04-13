package operations.issues;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7 as well as Task 2: Delete a periodical issue.
 * * <p>Note: Because of the ON DELETE CASCADE constraints defined in the schema,
 * deleting an issue will automatically remove its associated articles 
 * and publication links.</p>
 */
public class DeleteIssue extends Operation {

    /** The unique ID of the issue to be removed. */
    public int issueID;

    public DeleteIssue() {}

    /**
     * Executes the deletion of the issue.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Since issueID is an int, no quotes are needed in the SQL string.
        String sql = "DELETE FROM Issues WHERE issueID = " + issueID + ";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Issue " + issueID + " and all its articles have been deleted.");
            } else {
                System.out.println("Notice: No issue found with ID " + issueID + ".");
            }
        } catch (SQLException e) {
            // Pass the SQL error to the CLI via Tyler's custom exception
            throw new FailedOperationException("Failed to delete issue: " + e.getMessage());
        }
    }
}
