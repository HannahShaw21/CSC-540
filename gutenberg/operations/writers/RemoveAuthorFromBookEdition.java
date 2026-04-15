package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Remove a writer's authorship from a specific book edition.
 * <p>
 * This operation deletes the link between a writer and an ISBN in the 
 * 'Authorship' table. It does not delete the actual writer or book records.
 * </p>
 */
public class RemoveAuthorFromBookEdition extends Operation {

    /** The unique ID of the writer being removed. */
    public int writerID;

    /** The ISBN of the book edition. */
    public String isbn;

    /**
     * Default constructor for reflection-based instantiation.
     */
    public RemoveAuthorFromBookEdition() {}

    /**
     * Executes the SQL DELETE command to remove the authorship link.
     * * @param stmt The active JDBC Statement provided by the connection.
     * @throws FailedOperationException If the database operation fails or 
     * if no such relationship exists.
     */
    @Override
    public void run(Statement stmt) {
        // SQL Construction: ISBN is a string, so it needs double quotes.
        String sql = "DELETE FROM Authorship WHERE writerID = " + writerID + 
                     " AND isbn = \"" + isbn + "\";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("\n--- Authorship Removed ---");
                System.out.println("Success: Writer ID " + writerID + " is no longer linked to ISBN: " + isbn);
                System.out.println("--------------------------\n");
            } else {
                // If the query runs but finds nothing, we let the user know.
                System.out.println("Notice: No authorship record found for Writer " + writerID + 
                                   " and ISBN " + isbn + ". Nothing changed.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to remove author from book edition: " + e.getMessage());
        }
    }
}
