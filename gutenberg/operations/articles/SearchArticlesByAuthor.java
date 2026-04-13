package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.7: Retrieve a list of articles authored by a specific writer.
 * <p>
 * Performs a JOIN between the Articles and WritesArticle tables.
 * </p>
 */
public class SearchArticlesByAuthor extends Operation {

    /** The ID of the author whose articles are being searched. */
    public int writerID;

    public SearchArticlesByAuthor() {}

    /**
     * Executes a JOIN query to find all articles linked to the provided writerID.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If the search query fails.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "SELECT a.articleID, a.title, a.creationDate " +
                     "FROM Articles a " +
                     "JOIN WritesArticle w ON a.articleID = w.articleID " +
                     "WHERE w.writerID = " + writerID + ";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Search Results for Writer ID: " + writerID + " ---");
            System.out.printf("%-10s | %-30s | %-12s\n", "ID", "Title", "Date");
            System.out.println("------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-10d | %-30s | %-12s\n", 
                    rs.getInt("articleID"), rs.getString("title"), rs.getString("creationDate"));
            }
            if (!found) System.out.println("No articles found for this author.");
            System.out.println("------------------------------------------------------------\n");
        } catch (SQLException e) {
            throw new FailedOperationException("Author search failed: " + e.getMessage());
        }
    }
}
