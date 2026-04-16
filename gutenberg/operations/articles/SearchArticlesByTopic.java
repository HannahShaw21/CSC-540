package gutenberg.operations.articles;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT * FROM Articles " +
                        "WHERE topic LIKE \"%" + topic + "%\";";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getInt("issueID"));
                row.add(rs.getInt("articleNum"));
                row.add(rs.getString("title"));
                row.add(rs.getString("topic"));
                row.add(rs.getDate("writtenDate"));

                data.add(row);
            }

            Util.printTable("Articles with topic: " + topic,
                    List.of("Issue ID", "Article #", "Title", "Topic", "Written Date"),
                    data
            );
        } catch (SQLException e) {
            throw new FailedOperationException("Topic search failed: " + e.getMessage());
        }
    }
}
