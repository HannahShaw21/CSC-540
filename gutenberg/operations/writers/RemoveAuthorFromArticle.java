package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Remove a writer's authorship from a specific article.
 * <p>
 * This operation deletes the relationship in the 'WritesArticle' link table.
 * It is used to correct editorial assignments or remove contributors 
 * from a piece of periodical content.
 * </p>
 */
public class RemoveAuthorFromArticle extends Operation {

    /** The unique ID of the writer to be removed from the article. */
    public int writerID;

    /** The unique ID of the article in question. */
    public int articleID;

    /**
     * Default constructor for reflection-based instantiation.
     */
    public RemoveAuthorFromArticle() {}

    /**
     * Executes the SQL DELETE command on the WritesArticle table.
     * * @param stmt The active JDBC Statement provided by the connection.
     * @throws FailedOperationException If a database error occurs or the 
     * record does not exist.
     */
    @Override
    public void run(Statement stmt) {
        // SQL Construction: Both are integers, so no quotes are required.
        String sql = "DELETE FROM WritesArticle WHERE writerID = " + writerID + 
                     " AND articleID = " + articleID + ";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("\n--- Article Authorship Removed ---");
                System.out.println("Success: Writer ID " + writerID + 
                                   " is no longer linked to Article ID: " + articleID);
                System.out.println("------------------------------------\n");
            } else {
                // Helps catch typos during your CSC 540 demo
                System.out.println("Notice: No authorship record found for Writer " + writerID + 
                                   " and Article " + articleID + ". Nothing changed.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to remove author from article: " + e.getMessage());
        }
    }
}
