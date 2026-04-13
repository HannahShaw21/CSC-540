package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 1.1: Create a new publication record.
 * <p>
 * This operation adds a base publication (Book or Periodical) to the system.
 * It automatically retrieves the generated pubID for confirmation.
 * </p>
 */
public class CreateNewPublication extends Operation {

    public String title;
    public String topic;
    public String type;
    public Optional<String> periodicity = Optional.empty();

    public CreateNewPublication() {}

    /**
     * Executes the insertion and retrieves the auto-incremented ID.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the insertion fails or ID retrieval fails.
     */
    @Override
    public void run(Statement stmt) {
        // Handling the Optional periodicity for the SQL string
        String periodicityVal = periodicity.isPresent() ? "\"" + periodicity.get() + "\"" : "NULL";

        String sql = "INSERT INTO Publications (title, topic, type, periodicity) VALUES (" +
                     "\"" + title + "\", " + 
                     "\"" + topic + "\", " + 
                     "\"" + type + "\", " + 
                     periodicityVal + ");";

        try {
            stmt.executeUpdate(sql);
            
            // Retrieving the new ID assigned by the database
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID();");

            if (rs.next()) {
                int pubID = rs.getInt(1);
                System.out.println("\n--- Publication Created ---");
                System.out.println("Success: New entry added to registry.");
                System.out.println("Publication ID : " + pubID);
                System.out.println("Title          : " + title);
                System.out.println("Type           : " + type);
                System.out.println("---------------------------\n");
            } else {
                throw new FailedOperationException("Publication created, but failed to retrieve the new ID.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Could not create new publication: " + e.getMessage());
        }
    }
}
