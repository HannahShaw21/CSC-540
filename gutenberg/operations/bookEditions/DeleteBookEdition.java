package gutenberg.operations.bookEditions;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.3: Delete a book edition.
 * This operation removes a specific physical edition from the database.
 * * <p>Design Note: According to Report #2, the 'Chapters' and 'EditionOf' tables 
 * use ON DELETE CASCADE. Therefore, deleting an ISBN here will automatically 
 * clean up those related records in the database.</p>
 */
public class DeleteBookEdition extends Operation {

    /** The unique 13-character ISBN of the book edition to be removed. */
    public String isbn;

    public DeleteBookEdition() {}

    /**
     * Executes the deletion of the book edition.
     * * @param stmt The active JDBC Statement provided by the gutenberg.GutenbergConnection.
     * @throws FailedOperationException if any of the following occur:
     * <ul>
     * <li><b>SQL Syntax/Connectivity:</b> The database connection was interrupted.</li>
     * <li><b>Referential Integrity:</b> If a foreign key constraint (other than the 
     * cascaded ones) prevents deletion—for example, if an active 'Order' exists 
     * for this ISBN and is set to RESTRICT.</li>
     * </ul>
     */
    @Override
    public void run(Statement stmt) {
        // Using Double Quote to prevent SQL injection issues
        String sql = "DELETE FROM BookEditions WHERE isbn = \"" + isbn + "\";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Book edition '" + isbn + "' and its related data have been deleted.");
            } else {
                // If rowsAffected is 0, the ISBN didn't exist in the table
                System.out.println("Notice: No edition was found with ISBN '" + isbn + "'. Nothing was deleted.");
            }
        } catch (SQLException e) {
            // Throw custom exception to handle the error gracefully in the CLI
            throw new FailedOperationException("Failed to delete book edition: " + e.getMessage());
        }
    }
}
