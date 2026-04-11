package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 2.1: Enter new book edition.
 * This operation records a new physical edition of a book and establishes 
 * its relationship to the parent publication.
 * * <p>Design Decision: This class implements a two-step insertion process to 
 * maintain referential integrity between the BookEditions and EditionOf tables.</p>
 */
public class CreateNewBookEdition extends Operation {

    /** The unique publication ID from the Publications table. */
    public int pubID;
    
    /** The 13-character International Standard Book Number (Primary Key). */
    public String isbn;
    
    /** The edition number (e.g., 1 for first edition, 2 for second). */
    public int editionNum;
    
    /** The date the manuscript was completed. */
    public LocalDate writtenDate;
    
    /** The date the specific edition was published. */
    public LocalDate pubDate;

    /**
     * Default constructor for reflection-based instantiation.
     */
    public CreateNewBookEdition() {}

    /**
     * Executes the insertion of the book edition and its publication link.
     * * @param stmt The active JDBC Statement used to execute the updates.
     * @throws FailedOperationException if any of the following occur:
     * <ul>
     * <li><b>Primary Key Violation:</b> The ISBN provided already exists in the database.</li>
     * <li><b>Foreign Key Violation:</b> The provided pubID does not exist in the Publications table.</li>
     * <li><b>Data Integrity:</b> Dates are malformed or null.</li>
     * <li><b>Connectivity:</b> The database connection was lost mid-operation.</li>
     * </ul>
     */
    @Override
    public void run(Statement stmt) {
        String sqlBook = "INSERT INTO BookEditions (isbn, editionNum, pubDate, writtenDate) VALUES (" +
                         "'" + isbn + "', " + 
                         editionNum + ", " + 
                         "'" + pubDate.toString() + "', " + 
                         "'" + writtenDate.toString() + "');";

        String sqlLink = "INSERT INTO EditionOf (isbn, pubID) VALUES (" +
                         "'" + isbn + "', " + pubID + ");";

        try {
            // First, create the book edition record
            stmt.executeUpdate(sqlBook);
            
            // Second, link it to the publication
            stmt.executeUpdate(sqlLink);
            
            System.out.println("Success: New Book Edition '" + isbn + "' created and linked to Publication " + pubID + ".");
            
        } catch (SQLException e) {
            // We wrap the SQLException in FailedOperationException 
            // to provide a clear error message to the CLI user.
            throw new FailedOperationException("Failed to create book edition: " + e.getMessage());
        }
    }
}
