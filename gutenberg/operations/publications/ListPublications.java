package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.8 (General): List all publications in the database.
 * Displays a formatted overview of books and periodicals, including 
 * their topics and distribution frequency.
 */
public class ListPublications extends Operation {

    public ListPublications() {}

    /**
     * Executes the query and prints a master list of all publications.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database query fails.
     */
    @Override
    public void run(Statement stmt) {
        // Query to get everything, ordered by ID for a logical sequence
        String sql = "SELECT * FROM Publications ORDER BY pubID ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Master Publication Registry ---");
            // Header: ID (5), Title (25), Topic (15), Type (12), Periodicity (12)
            System.out.printf("%-5s | %-25s | %-15s | %-12s | %-12s\n", 
                              "ID", "Title", "Topic", "Type", "Frequency");
            System.out.println("-----------------------------------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String period = rs.getString("periodicity");
                
                System.out.printf("%-5d | %-25s | %-15s | %-12s | %-12s\n", 
                    rs.getInt("pubID"), 
                    rs.getString("title"), 
                    rs.getString("topic"), 
                    rs.getString("type"), 
                    (period == null || period.isEmpty()) ? "N/A" : period);
            }

            if (!hasData) {
                System.out.println("The publication registry is currently empty.");
            }
            System.out.println("-----------------------------------------------------------------------------------\n");

        } catch (SQLException e) {
            throw new FailedOperationException("Failed to retrieve publication list: " + e.getMessage());
        }
    }
}
