package gutenberg.operations.bookEditions;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Task 2.7: Search for book editions by topic.
 * Joins Publications and BookEditions via the EditionOf table.
 */
public class SearchBookEditionsByTopic extends Operation {

    public String topic;

    public SearchBookEditionsByTopic() {}

    @Override
    public void run(Statement stmt) {
        // Complex Join from Report 2
        String sql = "SELECT * FROM (((SELECT * FROM Publications WHERE topic LIKE \"%" + topic + "%\") relPubs " +
                "NATURAL JOIN EditionOf) NATURAL JOIN BookEditions);";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Books related to: " + topic + " ---");

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getInt("pubID"));
                row.add(rs.getString("isbn"));
                row.add(rs.getString("title"));
                row.add(rs.getInt("editionNum"));
                row.add(rs.getString("topic"));

                data.add(row);
            }

            Util.printTable("Books related to: " + topic,
                    List.of("pubID", "ISBN", "Title", "Edition #", "Topic"),
                    data
            );
        } catch (SQLException e) {
            throw new FailedOperationException("Topic search failed: " + e.getMessage());
        }
    }
}
