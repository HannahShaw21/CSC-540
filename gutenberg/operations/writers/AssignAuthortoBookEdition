package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.4: Assign a writer as an author of a specific book edition.
 * <p>
 * This operation creates a link in the 'Authorship' junction table. 
 * Both the writer and the book edition (ISBN) must already exist 
 * in the database for this to succeed.
 * </p>
 */
public class AssignAuthorToBookEdition extends Operation {

    /** The unique ID of the writer to be assigned. */
    public int writerID;

    /** The ISBN of the book edition the writer authored. */
    public String isbn;

    /**
     * Default constructor for reflection-based instantiation.
     */
    public AssignAuthorToBookEdition() {}

    /**
     * Executes the SQL INSERT to link a writer to a book edition.
     * * @param stmt The active JDBC Statement provided by the connection.
     * @throws FailedOperationException If the writerID or ISBN is invalid, 
     * or if the link already exists.
     */
    @Override
    public void run(Statement stmt) {
        // SQL Construction: Strings (ISBN) get double quotes; IDs (writerID) do not.
        String sql = "INSERT INTO Authorship (writerID, isbn) VALUES (" 
                     + writerID + ", \"" + isbn + "\");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("\n--- Authorship Assigned ---");
                System.out.println("Success: Writer ID " + writerID + " is now linked to ISBN: " + isbn);
                System.out.println("---------------------------\n");
            }
        } catch (SQLException e) {
            /*
             * This will trigger if:
             * 1. The ISBN doesn't exist in BookEditions.
             * 2. The writerID doesn't exist in Writers.
             * 3. This specific pair is already in the table (Duplicate entry).
             */
            throw new FailedOperationException("Failed to assign authorship. Ensure IDs are correct and not already linked. Error: " + e.getMessage());
        }
    }
}
