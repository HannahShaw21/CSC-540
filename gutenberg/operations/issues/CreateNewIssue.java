package gutenberg.operations.issues;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Task 2.1: Enter new periodical issue.
 * Matches the 'Issues' and 'IssueOf' tables from Project Report #2.
 * This operation is wrapped in a transaction to ensure referential integrity.
 */
public class CreateNewIssue extends Operation {

    /** The ID of the parent Publication this issue belongs to. */
    public int pubID;
    
    /** The number of the issue within the periodical series. */
    public int issueNum;
    
    /** Optional title for the specific issue. */
    public Optional<String> issueTitle = Optional.empty();
    
    /** The date this specific issue was released. */
    public LocalDate pubDate;

    public CreateNewIssue() {}

    /**
     * Executes the creation of a new issue and its link to a publication.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the pubID is invalid or if the 
     * transaction fails due to a database error.
     */
    @Override
    public void run(Statement stmt) {
        String sqlPubValidation = String.format("SELECT type FROM Publications WHERE pubID=%d;", pubID);

        // SQL 1: Insert the basic issue data
        String sqlIssue = "INSERT INTO Issues (issueNum, issueTitle, pubDate) VALUES (" +
                          issueNum + ", " + Util.sqlStrWrapper(issueTitle) + ", \"" + pubDate.toString() + "\");";
        
        // SQL 2: Link the new Issue to the Publication (IssueOf table)
        // We use LAST_INSERT_ID() to get the ID MariaDB just generated for the Issue.
        String sqlLink = "INSERT INTO IssueOf (issueID, pubID) VALUES (LAST_INSERT_ID(), " + pubID + ");";

        try {
            // Start the transaction
            stmt.executeUpdate("START TRANSACTION;");

            // Ensure that a publication of type book exists with the given pubID
            ResultSet rs = stmt.executeQuery(sqlPubValidation);
            if (!rs.next())
                throw new FailedOperationException("No publication found with that ID");

            if (rs.getString("type").equals("book"))
                throw new FailedOperationException("Publication #" + pubID + " is not a periodical.");

            // Execute both steps
            stmt.executeUpdate(sqlIssue);
            stmt.executeUpdate(sqlLink);

            // If we got here, everything is perfect. Save it!
            stmt.executeUpdate("COMMIT;");
            
            System.out.println("Success: Issue #" + issueNum + " created and linked to Publication " + pubID + ".");

        } catch (SQLException e) {
            try {
                // If any part failed, undo all changes
                stmt.executeUpdate("ROLLBACK;");
            } catch (SQLException rollbackEx) {
                System.err.println("Critical failure: Could not rollback transaction.");
            }
            // Pass the error back to the CLI
            throw new FailedOperationException("Failed to create issue. Transaction rolled back. Error: " + e.getMessage());
        }
    }
}
