package gutenberg.operations.chapters;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 2.5: Update existing chapter content or title.
 */
public class UpdateChapter extends Operation {

    // Identity fields (The "Where" clause)
    public String isbn;
    public int chapterNum;

    // Optional update fields
    public Optional<String> title = Optional.empty();
    public Optional<String> content = Optional.empty();

    public UpdateChapter() {}

    @Override
    public void run(Statement stmt) {
        if (title.isEmpty() && content.isEmpty()) {
            throw new FailedOperationException("Specify a new title or new content path to update.");
        }

        StringBuilder sql = new StringBuilder("UPDATE Chapters SET");
        boolean notFirst = false;

        if (title.isPresent()) {
            sql.append(" title = \"").append(title.get()).append("\"");
            notFirst = true;
        }
        if (content.isPresent()) {
            if (notFirst) sql.append(",");
            sql.append(" content = \"").append(content.get()).append("\"");
        }

        sql.append(" WHERE isbn = \"").append(isbn).append("\" AND chapterNum = ").append(chapterNum).append(";");

        try {
            int rowsAffected = stmt.executeUpdate(sql.toString());
            if (rowsAffected > 0) {
                System.out.println("Success: Chapter " + chapterNum + " updated.");
            } else {
                System.out.println("Notice: No matching chapter found to update.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Update failed: " + e.getMessage());
        }
    }
}
