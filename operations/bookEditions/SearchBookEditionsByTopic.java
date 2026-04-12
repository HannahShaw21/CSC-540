package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        String sql = "SELECT * FROM BookEditions WHERE isbn IN (" +
                     "SELECT isbn FROM (SELECT * FROM Publications WHERE topic LIKE \"%" + topic + "%\") relPubs " +
                     "NATURAL JOIN EditionOf);";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Books related to: " + topic + " ---");
            System.out.printf("%-15s | %-10s\n", "ISBN", "Edition #");
            System.out.println("----------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-15s | %-10d\n", rs.getString("isbn"), rs.getInt("editionNum"));
            }
            if (!found) System.out.println("No books found for this topic.");
            System.out.println("----------------------------\n");
        } catch (SQLException e) {
            throw new FailedOperationException("Topic search failed: " + e.getMessage());
        }
    }
}
