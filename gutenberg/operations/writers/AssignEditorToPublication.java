package gutenberg.operations.writers;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.4: Assign a writer as an editor for a specific publication.
 * <p>
 * This operation manages the relationship in the 'Edits' link table.
 * It requires both the writer and the publication to exist in their 
 * respective master tables.
 * </p>
 */
public class AssignEditorToPublication extends Operation {

    /** The ID of the writer being assigned as an editor. */
    public int writerID;

    /** The ID of the publication they are being assigned to. */
    public int pubID;

    public AssignEditorToPublication() {}

    /**
     * Executes the assignment into the 'Edits' table.
     * @param stmt The active JDBC Statement used to communicate with the database.
     * @throws FailedOperationException if the writerID or pubID do not exist.
     */
    @Override
    public void run(Statement stmt) {
        // Since both are integers, we do not need to wrap them in double quotes.
        String sql = "INSERT INTO Edits (writerID, pubID) VALUES (" 
                     + writerID + ", " + pubID + ");";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("Success: Writer ID " + writerID + 
                                   " has been assigned as an editor for Publication ID " + pubID + ".");
            }
        } catch (SQLException e) {
            /* * This catch block handles Foreign Key violations. If the database 
             * rejects the insert, it's likely because the writerID or pubID 
             * provided by the user does not exist in the parent tables.
             */
            throw new FailedOperationException("Failed to assign editor. Ensure both Writer ID " 
                                               + writerID + " and Publication ID " + pubID + 
                                               " are valid. Error: " + e.getMessage());
        }
    }
}
