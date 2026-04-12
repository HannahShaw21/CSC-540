package operations.chapters;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 2.4: Enter new content (Chapters).
 * Inserts a new chapter associated with an existing Book Edition.
 */
public class CreateNewChapter extends Operation {

    public String isbn;
    public int chapterNum;
    public Optional<String> title = Optional.empty();
    public String content;

    public CreateNewChapter() {}

    /**
     * @throws FailedOperationException if the ISBN does not exist (FK Violation) 
     * or if the chapter number is already used for this book (PK Violation).
     */
    @Override
    public void run(Statement stmt) {
        String chapterTitle = title.isPresent() ? "\"" + title.get() + "\"" : "NULL";
        
        String sql = "INSERT INTO Chapters (isbn, chapterNum, title, content) VALUES (" +
                     "\"" + isbn + "\", " + 
                     chapterNum + ", " + 
                     chapterTitle + ", " + 
                     "\"" + content + "\");";

        try {
            stmt.executeUpdate(sql);
            System.out.println("Success: Chapter " + chapterNum + " added to Book " + isbn);
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to add chapter: " + e.getMessage());
        }
    }
}
