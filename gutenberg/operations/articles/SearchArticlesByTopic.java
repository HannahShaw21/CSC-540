package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.7: Filter articles based on the topic of their parent publication.
 * <p>
 * Joins Articles, Issues, and Publications to filter by the 'topic' column.
 * </p>
 */
public class SearchArticlesByTopic extends Operation {

    /** The topic string (e.g., 'Science', 'Politics') to filter by. */
    public String topic;

    public SearchArticlesByTopic() {}

    /**
     * Executes a triple-join query to retrieve articles by publication topic.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If the topic is invalid or database fails.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "SELECT a.articleID, a.title, p.title AS pubTitle " +
                     "FROM Articles a " +
                     "JOIN Issues i ON a.issueID = i.issueID " +
                     "JOIN Publications p ON i.pubID = p.pubID " +
                     "WHERE p.topic = \"" + topic + "\";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Articles Matching Topic: " + topic + " ---");
            System.out.printf("%-10s | %-25s | %-25s\n", "ID", "Article Title", "Publication Name");
            System.out.println("-------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-10d | %-25s | %-25s\n", 
                    rs.getInt("articleID"), rs.getString("title"), rs.getString("pubTitle"));
            }
            if (!found) System.out.println("No articles found under this topic.");
            System.out.println("-------------------------------------------------------------------\n");
        } catch (SQLException e) {
            throw new FailedOperationException("Topic search failed: " + e.getMessage());
        }
    }
}
