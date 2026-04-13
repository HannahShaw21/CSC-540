package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.4: Assign a writer as an author for a specific article.
 * <p>
 * This operation records the relationship in the 'WritesArticle' junction table.
 * It ensures that the contributor is officially linked to their periodical content.
 * </p>
 */
public class AssignAuthorToArticle extends Operation {

    /** The unique ID of the writer/author. */
    public int writerID;

    /** The unique ID of the article being assigned. */
    public int articleID;

    /**
     * Default constructor for reflection-based instantiation.
     */
    public AssignAuthorToArticle() {}

    /**
     * Executes the SQL INSERT to link a writer to an article.
     * * @param stmt The active JDBC Statement provided by the connection.
     * @throws FailedOperationException If the writerID or articleID is invalid, 
     * or if the link already exists in the database.
     */
    @Override
    public void run(Statement stmt) {
        // SQL Construction: Both are integers, so no quotes are needed.
        // Table name 'WritesArticle' matches the schema defined in Project Report #2.
        String sql = "INSERT INTO WritesArticle (writerID, articleID) VALUES (" 
                     + writerID + ", " + articleID + ");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("\n--- Article Authorship Assigned ---");
                System.out.println("Success: Writer ID " + writerID + " is now linked to Article ID: " + articleID);
                System.out.println("------------------------------------\n");
            }
        } catch (SQLException e) {
            /*
             * Foreign Key Constraint: This will fail if writerID or articleID 
             * do not exist in their respective master tables.
             */
            throw new FailedOperationException("Failed to assign article author. " +
                    "Ensure both Writer ID and Article ID exist. Error: " + e.getMessage());
        }
    }
}
