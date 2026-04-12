package operations.chapters;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 1.7: Delete a chapter.
 * Chapters are identified by a composite key of ISBN and Chapter Number.
 */
public class DeleteChapter extends Operation {

    public String isbn;
    public int chapterNum;

    public DeleteChapter() {}

    /**
     * Executes the deletion.
     * @param stmt The active Statement.
     * @throws FailedOperationException if the specific chapter does not exist.
     */
    @Override
    public void run(Statement stmt) {
        // Double-quote rule: ISBN needs quotes, chapterNum (INT) does not.
        String sql = "DELETE FROM Chapters WHERE isbn = \"" + isbn + "\" AND chapterNum = " + chapterNum + ";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("Success: Chapter " + chapterNum + " of Book " + isbn + " deleted.");
            } else {
                System.out.println("Notice: No chapter found with those details.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to delete chapter: " + e.getMessage());
        }
    }
}
