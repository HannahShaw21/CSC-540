package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 2.2: Update metadata for an existing publication.
 * <p>
 * This operation allows for partial updates to a publication's title, 
 * topic, or periodicity. It uses the pubID to locate the record.
 * </p>
 */
public class UpdatePublication extends Operation {

    /** The ID of the publication to update. */
    public int pubID;
    
    public Optional<String> title = Optional.empty();
    public Optional<String> topic = Optional.empty();
    public Optional<String> periodicity = Optional.empty();

    public UpdatePublication() {}

    /**
     * Builds and executes a dynamic SQL UPDATE statement.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if no fields are specified or the SQL fails.
     */
    @Override
    public void run(Statement stmt) {
        // Guard clause: Ensure there's actually a change to make
        if (title.isEmpty() && topic.isEmpty() && periodicity.isEmpty()) {
            throw new FailedOperationException("No update fields provided. Please specify a new title, topic, or periodicity.");
        }

        StringBuilder sql = new StringBuilder("UPDATE Publications SET ");
        boolean notFirst = false;

        // Dynamic SQL construction
        if (title.isPresent()) {
            sql.append("title = \"").append(title.get()).append("\"");
            notFirst = true;
        }
        if (topic.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("topic = \"").append(topic.get()).append("\"");
            notFirst = true;
        }
        if (periodicity.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("periodicity = \"").append(periodicity.get()).append("\"");
        }

        sql.append(" WHERE pubID = ").append(pubID).append(";");

        try {
            int rowsAffected = stmt.executeUpdate(sql.toString());
            
            if (rowsAffected > 0) {
                System.out.println("Success: Publication " + pubID + " has been updated.");
            } else {
                // This triggers if the pubID is valid syntax-wise but doesn't exist in the table
                throw new FailedOperationException("No publication found with ID " + pubID + ". Update aborted.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to update publication: " + e.getMessage());
        }
    }
}
