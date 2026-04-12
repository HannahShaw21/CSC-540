package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 2.6: Search for book editions by publication date range.
 */
public class SearchBookEditionsByDate extends Operation {

    public LocalDate startDate;
    public LocalDate endDate;

    public SearchBookEditionsByDate() {}

    /**
     * Executes a search for editions published within a specific range.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database connection fails.
     */
    @Override
    public void run(Statement stmt) {
        // Double quote rule applied to date strings
        String sql = "SELECT * FROM BookEditions WHERE pubDate BETWEEN \"" + 
                     startDate.toString() + "\" AND \"" + endDate.toString() + "\";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Books Published Between " + startDate + " and " + endDate + " ---");
            System.out.printf("%-15s | %-10s | %-12s\n", "ISBN", "Edition #", "Pub Date");
            System.out.println("--------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-15s | %-10d | %-12s\n", 
                    rs.getString("isbn"), rs.getInt("editionNum"), rs.getString("pubDate"));
            }
            if (!found) System.out.println("No records found in this date range.");
            System.out.println("--------------------------------------------------\n");
        } catch (SQLException e) {
            throw new FailedOperationException("Date search failed: " + e.getMessage());
        }
    }
}
