package operations.writers;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.8 (General): List all writers in the system.
 * Retrieves and displays a complete roster of writers and their current status.
 */
public class ListWriters extends Operation {

    public ListWriters() {}

    /**
     * Executes the query to fetch all writers.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database query fails.
     */
    @Override
    public void run(Statement stmt) {
        // Simple query to retrieve all records from the Writers table
        String sql = "SELECT * FROM Writers ORDER BY writerID ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Registered Writers Roster ---");
            // Formatting: ID (5), Name (25), Status (15)
            System.out.printf("%-5s | %-25s | %-15s\n", "ID", "Name", "Status");
            System.out.println("---------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-5d | %-25s | %-15s\n", 
                    rs.getInt("writerID"), 
                    rs.getString("name"), 
                    rs.getString("staffOrInvited"));
            }

            if (!hasData) {
                System.out.println("The writer registry is currently empty.");
            }
            System.out.println("---------------------------------------------------------\n");

        } catch (SQLException e) {
            // Standardizing error handling across the operations package
            throw new FailedOperationException("Failed to retrieve writer list: " + e.getMessage());
        }
    }
}
