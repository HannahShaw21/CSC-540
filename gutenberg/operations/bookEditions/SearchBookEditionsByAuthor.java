package gutenberg.operations.bookEditions;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.8: Search for book editions by author name.
 * Performs a join between Writers, Authors, and BookEditions.
 */
public class SearchBookEditionsByAuthor extends Operation {

    /** The name (or partial name) of the author to search for. */
    public String authorName;

    public SearchBookEditionsByAuthor() {}

    @Override
    public void run(Statement stmt) {
        // SQL from Report #2: Using LIKE and % for partial matches
        String sql = "SELECT * FROM BookEditions WHERE isbn IN (" +
                     "  SELECT isbn FROM (SELECT * FROM Writers WHERE name LIKE \"%" + authorName + "%\") w1 " +
                     "  NATURAL JOIN Authors" +
                     ");";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Search Results for Author: " + authorName + " ---");
            System.out.printf("%-15s | %-10s | %-12s | %-12s\n", "ISBN", "Edition #", "Pub Date", "Written Date");
            System.out.println("------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-15s | %-10d | %-12s | %-12s\n", 
                    rs.getString("isbn"), 
                    rs.getInt("editionNum"), 
                    rs.getString("pubDate"), 
                    rs.getString("writtenDate"));
            }

            if (!found) {
                System.out.println("No book editions found for that author.");
            }
            System.out.println("------------------------------------------------------------\n");

        } catch (SQLException e) {
            throw new FailedOperationException("Search failed: " + e.getMessage());
        }
    }
}
