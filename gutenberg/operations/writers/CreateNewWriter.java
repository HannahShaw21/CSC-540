package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.3 and Task 2: Enter a new writer into the system.
 * This operation adds a new individual to the Writers table, 
 * categorized as either 'Staff' or 'Invited'.
 */
public class CreateNewWriter extends Operation {

    /** The full name of the writer. */
    public String name;

    /** The status of the writer (e.g., 'Staff' or 'Invited'). */
    public String staffOrInvited;

    public CreateNewWriter() {}

    /**
     * Executes the insertion of a new writer record.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs or 
     * if the writer category is invalid.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "INSERT INTO Writers (name, staffOrInvited) VALUES (" +
                     "\"" + name + "\", " + 
                     "\"" + staffOrInvited + "\");";

        try {
            stmt.executeUpdate(sql);
            System.out.println("Success: Writer '" + name + "' added as " + staffOrInvited + ".");
        } catch (SQLException e) {
            // Check for specific database errors, like duplicate names if that's a constraint
            throw new FailedOperationException("Failed to create new writer: " + e.getMessage());
        }
    }
}
