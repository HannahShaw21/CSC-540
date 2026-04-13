package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 2.2: Perform partial updates on an existing article record.
 */
public class UpdateArticle extends Operation {

    /** The ID of the article to be modified. */
    public int articleID;

    /** Optional new title for the article. */
    public Optional<String> title = Optional.empty();

    /** Optional new text content for the article. */
    public Optional<String> content = Optional.empty();

    /** Optional new date of authorship. */
    public Optional<String> writtenDate = Optional.empty();

    public UpdateArticle() {}

    /**
     * Dynamically builds an UPDATE statement based on which Optional fields are present.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If no fields are provided or Article ID is missing.
     */
    @Override
    public void run(Statement stmt) {
        if (title.isEmpty() && content.isEmpty() && writtenDate.isEmpty()) {
            throw new FailedOperationException("No update fields provided for Article ID " + articleID);
        }

        StringBuilder sql = new StringBuilder("UPDATE Articles SET ");
        boolean notFirst = false;

        if (title.isPresent()) {
            sql.append("title = \"").append(title.get()).append("\"");
            notFirst = true;
        }
        if (content.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("content = \"").append(content.get()).append("\"");
            notFirst = true;
        }
        if (writtenDate.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("creationDate = \"").append(writtenDate.get()).append("\"");
        }

        sql.append(" WHERE articleID = ").append(articleID).append(";");

        try {
            int rows = stmt.executeUpdate(sql.toString());
            if (rows > 0) {
                System.out.println("Success: Article " + articleID + " has been updated.");
            } else {
                throw new FailedOperationException("No article found with ID " + articleID);
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Update failed: " + e.getMessage());
        }
    }
}
