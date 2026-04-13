package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 2.7: Find articles written on a specific date.
 */
public class SearchArticlesByDate extends Operation {

    /** The date to search for in the Articles registry. */
    public LocalDate searchDate;

    public SearchArticlesByDate() {}

    /**
     * Queries the database for all articles matching the provided creationDate.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If the date format is invalid or query fails.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "SELECT * FROM Articles WHERE creationDate = \"" + searchDate.toString() + "\";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Articles Written On: " + searchDate + " ---");
            System.out.printf("%-10s | %-30s | %-10s\n", "ID", "Title", "Issue ID");
            System.out.println("------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-10d | %-30s | %-10d\n", 
                    rs.getInt("articleID"), rs.getString("title"), rs.getInt("issueID"));
            }
            if (!found) System.out.println("No articles found for this date.");
            System.out.println("------------------------------------------------------------\n");
        } catch (SQLException e) {
            throw new FailedOperationException("Date search failed: " + e.getMessage());
        }
    }
}
