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
 * Task 2.2: Update information for an existing periodical issue.
 * Allows partial updates to the issue number, title, or publication date.
 */
public class UpdateIssue extends Operation {

    /** The ID of the issue to update. */
    public int issueID;

    /** Optional fields for update. */
    public Optional<Integer> newIssueNum = Optional.empty();
    public Optional<String> newTitle = Optional.empty();
    public Optional<LocalDate> newPubDate = Optional.empty();

    public UpdateIssue() {}

    /**
     * Dynamically builds and executes the update query.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if no update fields are provided 
     * or a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Validation: Ensure there's actually something to update
        if (newIssueNum.isEmpty() && newTitle.isEmpty() && newPubDate.isEmpty()) {
            throw new FailedOperationException("Please provide at least one field to update (Issue Num, Title, or Date).");
        }

        StringBuilder sql = new StringBuilder("UPDATE Issues SET");
        boolean addedField = false;

        if (newIssueNum.isPresent()) {
            sql.append(" issueNum = ").append(newIssueNum.get());
            addedField = true;
        }

        if (newTitle.isPresent()) {
            if (addedField) sql.append(",");
            sql.append(" issueTitle = ").append(Util.sqlStrWrapper(newTitle.get()));
            addedField = true;
        }

        if (newPubDate.isPresent()) {
            if (addedField) sql.append(",");
            // Dates also need double quotes
            sql.append(" pubDate = \"").append(newPubDate.get()).append("\"");
        }

        sql.append(" WHERE issueID = ").append(issueID).append(";");

        try {
            int rowsAffected = stmt.executeUpdate(sql.toString());

            if (rowsAffected > 0) {
                System.out.println("Success: Issue " + issueID + " has been updated.");
            } else {
                System.out.println("Notice: No issue found with ID " + issueID + ". No changes made.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to update issue: " + e.getMessage());
        }
    }
}
