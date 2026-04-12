package operations.articles;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DeleteArticle extends Operation {

    public int issueID;
    public int articleNum;

    public DeleteArticle() {}

    @Override
    public void run(Statement stmt) {
        String sql = "DELETE FROM articles WHERE articleNum = \"" + articleNum + "\";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Article '" + articleNum + "' and its related data have been deleted.");
            } else {
                // If rowsAffected is 0, the articleNum didn't exist in the table
                System.out.println("Notice: No article was found with articleNum '" + articleNum + "'. Nothing was deleted.");
            }
        } catch (SQLException e) {
            // Throw custom exception to handle the error gracefully in the CLI
            throw new FailedOperationException("Failed to delete article: " + e.getMessage());
        }
    }
}
