package gutenberg.operations.issues;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.12: Compare the article contents of two different periodical issues.
 * Displays a side-by-side list of articles published in each issue.
 */
public class CompareIssues extends Operation {

    /** The ID of the first issue to compare. */
    public int firstIssueID;

    /** The ID of the second issue to compare. */
    public int secondIssueID;

    public CompareIssues() {}

    /**
     * Executes the comparison query.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database access error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // We select all articles from both issues, ordered so they appear grouped
        String sql = "SELECT issueID, articleNum, title FROM Articles " +
                     "WHERE issueID = " + firstIssueID + " OR issueID = " + secondIssueID + " " +
                     "ORDER BY issueID, articleNum;";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Comparison: Issue " + firstIssueID + " vs Issue " + secondIssueID + " ---");
            System.out.printf("%-10s | %-5s | %-30s\n", "Issue ID", "Art #", "Article Title");
            System.out.println("-----------------------------------------------------------------");

            boolean foundAny = false;
            while (rs.next()) {
                foundAny = true;
                System.out.printf("%-10d | %-5d | %-30s\n", 
                    rs.getInt("issueID"), 
                    rs.getInt("articleNum"), 
                    rs.getString("title"));
            }

            if (!foundAny) {
                System.out.println("No articles found in either specified issue.");
            }
            System.out.println("-----------------------------------------------------------------\n");

        } catch (SQLException e) {
            // Throwing exception so the CLI handles the error gracefully
            throw new FailedOperationException("Failed to compare issues: " + e.getMessage());
        }
    }
}
