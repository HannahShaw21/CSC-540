package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 2.1: Enter new book edition.
 * Uses a Transaction to ensure that both the BookEditions entry and 
 * the EditionOf link are created atomically.
 */
public class CreateNewBookEdition extends Operation {

    public int pubID;
    public String isbn;
    public int editionNum;
    public LocalDate writtenDate;
    public LocalDate pubDate;

    public CreateNewBookEdition() {}

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
            // 1. Start the transaction
            stmt.executeUpdate("START TRANSACTION;");

            // 2. Run the first part (Create the Book)
            stmt.executeUpdate(sqlBook);
            
            // 3. Run the second part (Link to Publication)
            stmt.executeUpdate(sqlLink);
            
            // 4. If we got here, everything worked! Save changes permanently.
            stmt.executeUpdate("COMMIT;");
            
            System.out.println("Success: Book Edition and Publication Link created atomically.");
            
        } catch (SQLException e) {
            try {
                // 5. If ANY step failed, undo everything inside this transaction
                stmt.executeUpdate("ROLLBACK;");
            } catch (SQLException rollbackEx) {
                // This would only happen if the database connection itself died
                System.err.println("Critical failure: Could not rollback transaction.");
            }
            
            // Throw Tyler's exception so the CLI knows the operation failed
            throw new FailedOperationException("Transaction failed. No data was changed. Error: " + e.getMessage());
        }
    }
}
