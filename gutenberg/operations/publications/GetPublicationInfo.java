package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.1: Retrieve and display detailed information for a specific publication.
 * <p>
 * This operation fetches all attributes from the 'Publications' table
 * based on the provided pubID.
 * </p>
 */
public class GetPublicationInfo extends Operation {

    /** The unique ID of the publication to look up. */
    public int pubID;

    public GetPublicationInfo() {}

    /**
     * Executes the query and prints the publication details.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the pubID is not found or a SQL error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Simple SELECT based on the Primary Key
        String sql = "SELECT * FROM Publications WHERE pubID = " + pubID + ";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                System.out.println("\n--- Publication Record ---");
                System.out.printf("ID          : %d\n", rs.getInt("pubID"));
                System.out.printf("Title       : %s\n", rs.getString("title"));
                System.out.printf("Topic       : %s\n", rs.getString("topic"));
                System.out.printf("Type        : %s\n", rs.getString("type"));
                
                // Handle optional periodicity field
                String period = rs.getString("periodicity");
                System.out.printf("Periodicity : %s\n", (period == null || period.isEmpty()) ? "N/A" : period);
                System.out.println("--------------------------\n");
            } else {
                throw new FailedOperationException("No publication found with ID " + pubID + ".");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to retrieve publication info: " + e.getMessage());
        }
    }
}
