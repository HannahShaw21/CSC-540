package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Task 2.2: Update information for an existing book edition.
 * This operation allows for partial updates of edition metadata 
 * identified by its ISBN.
 */
public class UpdateBookEdition extends Operation {

    /** The ISBN of the edition to be updated (Primary Key). */
    public String isbn;

    /** Optional fields: only the fields provided by the user will be updated. */
    public Optional<Integer> editionNum = Optional.empty();
    public Optional<LocalDate> pubDate = Optional.empty();
    public Optional<LocalDate> writtenDate = Optional.empty();

    public UpdateBookEdition() {}

    /**
     * Executes the UPDATE statement dynamically based on provided fields.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if no fields are specified for update 
     * or if the ISBN does not exist.
     */
    @Override
    public void run(Statement stmt) {
        if (editionNum.isEmpty() && pubDate.isEmpty() && writtenDate.isEmpty()) {
            throw new FailedOperationException("Please specify at least one field to update.");
        }

        StringBuilder sql = new StringBuilder("UPDATE BookEditions SET");
        boolean notFirst = false;

        if (editionNum.isPresent()) {
            sql.append(" editionNum = ").append(editionNum.get());
            notFirst = true;
        }
        if (pubDate.isPresent()) {
            if (notFirst) sql.append(",");
            sql.append(" pubDate = \"").append(pubDate.get().toString()).append("\"");
            notFirst = true;
        }
        if (writtenDate.isPresent()) {
            if (notFirst) sql.append(",");
            sql.append(" writtenDate = \"").append(writtenDate.get().toString()).append("\"");
        }

        sql.append(" WHERE isbn = \"").append(isbn).append("\";");

        try {
            int rowsAffected = stmt.executeUpdate(sql.toString());
            if (rowsAffected > 0) {
                System.out.println("Success: Book Edition '" + isbn + "' updated.");
            } else {
                System.out.println("Notice: No book found with ISBN '" + isbn + "'.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Update failed: " + e.getMessage());
        }
    }
}
