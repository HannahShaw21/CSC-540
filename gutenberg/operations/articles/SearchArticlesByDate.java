package gutenberg.operations.articles;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Task 2.7: Find articles written on a specific date.
 */
public class SearchArticlesByDate extends Operation {

    /** The date to search for in the Articles registry. */
    public LocalDate startDate;
    public LocalDate endDate;

    public SearchArticlesByDate() {}

    /**
     * Queries the database for all articles matching the provided creationDate.
     * * @param stmt The active JDBC Statement.
     * @throws FailedOperationException If the date format is invalid or query fails.
     */
    @Override
    public void run(Statement stmt) {
        String sql = "SELECT * FROM Articles WHERE writtenDate BETWEEN " +
                "\"" + startDate.toString() + "\" AND \"" + endDate + "\";";

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

            Util.printTable("Articles written between " + startDate + " and " + endDate,
                    List.of("Issue ID", "Article #", "Title", "Topic", "Written Date"),
                    data
            );
        } catch (SQLException e) {
            throw new FailedOperationException("Date search failed: " + e.getMessage());
        }
    }
}
