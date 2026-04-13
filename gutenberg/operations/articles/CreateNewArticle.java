package gutenberg.operations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Task 1.2: Enter a new article record for a specific periodical issue.
 * <p>
 * This operation records the article's position in the issue (articleNum),
 * its title, and the date it was written.
 * </p>
 */
public class CreateNewArticle extends Operation {

    public int issueID;
    public int articleNum;
    public String title;
    public LocalDate writtenDate;
    public String content;

    public CreateNewArticle() {}

    /**
     * Executes the SQL INSERT and retrieves the auto-generated articleID.
     * @param stmt The active JDBC Statement.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "INSERT INTO Articles (issueID, articleNum, title, creationDate, content) VALUES (" +
                     issueID + ", " + 
                     articleNum + ", " + 
                     "\"" + title + "\", " + 
                     "\"" + writtenDate.toString() + "\", " + 
                     "\"" + content + "\");";

        try {
            stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID();");

            if (rs.next()) {
                System.out.println("\n--- Article Creation Success ---");
                System.out.println("New Article ID : " + rs.getInt(1));
                System.out.println("Issue ID       : " + issueID);
                System.out.println("Title          : " + title);
                System.out.println("--------------------------------\n");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to create article. Check if Issue " + issueID + " exists. Error: " + e.getMessage());
        }
    }
}
