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
 * Task 2.7: Retrieve a list of articles authored by a specific writer.
 * <p>
 * Performs a JOIN between the Articles and WritesArticle tables.
 * </p>
 */
public class SearchArticlesByAuthor extends Operation {

    /** The ID of the author whose articles are being searched. */
    public String writerName;

    public SearchArticlesByAuthor() {}

    /**
     * Executes a JOIN query to find all articles linked to the provided writerID.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If the search query fails.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "SELECT * FROM (Articles NATURAL JOIN " +
                "(SELECT writerID, issueID, articleNum FROM Authors) authorWorks " +
                "NATURAL JOIN (SELECT writerID, name AS authorName FROM Writers WHERE name LIKE " +
                "\"%" + writerName + "%\") relAuthors);";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getInt("issueID"));
                row.add(rs.getInt("articleNum"));
                row.add(rs.getString("title"));
                row.add(rs.getString("topic"));
                row.add(rs.getString("authorName"));

                data.add(row);
            }

            Util.printTable("Articles authored by: " + writerName,
                    List.of("Issue ID", "Article #", "Title", "Topic", "Author"),
                    data
            );
        } catch (SQLException e) {
            throw new FailedOperationException("Author search failed: " + e.getMessage());
        }
    }
}
