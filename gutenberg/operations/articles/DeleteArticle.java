package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Remove an article record from the database.
 * <p>
 * This operation deletes an article entry based on its unique ID.
 * </p>
 */
public class DeleteArticle extends Operation {

    /** The unique ID of the article to be deleted. */
    public int articleID;

    public DeleteArticle() {}

    /**
     * Executes the SQL DELETE command on the Articles table.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If the articleID is not found or is linked to other records.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "DELETE FROM Articles WHERE articleID = " + articleID + ";";

        try {
            int rows = stmt.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("Success: Article " + articleID + " has been removed.");
            } else {
                throw new FailedOperationException("No article found with ID " + articleID);
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Could not delete article: " + e.getMessage());
        }
    }
}
