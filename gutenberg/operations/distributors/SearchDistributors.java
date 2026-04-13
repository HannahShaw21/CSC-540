package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 2.7 (Distributors): Search for distributors based on type and/or location.
 * Uses optional filters to narrow down the distributor directory.
 */
public class SearchDistributors extends Operation {

    /** Optional filter for the distributor type (e.g., 'Wholesale', 'Retail'). */
    public Optional<String> type = Optional.empty();
    
    /** Optional filter for the city where the distributor is located. */
    public Optional<String> city = Optional.empty();

    public SearchDistributors() {}

    /**
     * Executes a dynamic search query and prints the results in a formatted table.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Base query
        StringBuilder sql = new StringBuilder("SELECT * FROM Distributors WHERE 1=1");

        // Dynamically add filters based on user input
        if (type.isPresent()) {
            sql.append(" AND type = \"").append(type.get()).append("\"");
        }
        if (city.isPresent()) {
            sql.append(" AND city = \"").append(city.get()).append("\"");
        }

        sql.append(" ORDER BY distributorName ASC;");

        try {
            ResultSet rs = stmt.executeQuery(sql.toString());
            
            System.out.println("\n--- Distributor Search Results ---");
            System.out.printf("%-20s | %-10s | %-15s | %-15s\n", 
                              "Name", "Type", "City", "Contact");
            System.out.println("-------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-20s | %-10s | %-15s | %-15s\n", 
                    rs.getString("distributorName"),
                    rs.getString("type"),
                    rs.getString("city"),
                    rs.getString("contactPerson") == null ? "N/A" : rs.getString("contactPerson"));
            }

            if (!found) {
                System.out.println("No distributors found matching those criteria.");
            }
            System.out.println("-------------------------------------------------------------------\n");

        } catch (SQLException e) {
            throw new FailedOperationException("Search failed: " + e.getMessage());
        }
    }
}
