package gutenberg.operations.publications;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Task 2.8 (General): List all publications in the database.
 * Displays a formatted overview of books and periodicals, including 
 * their topics and distribution frequency.
 */
public class ListPublications extends Operation {

    public ListPublications() {}

    /**
     * Executes the query and prints a master list of all publications.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database query fails.
     */
    @Override
    public void run(Statement stmt) {
        // Query to get everything, ordered by ID for a logical sequence
        String sql = "SELECT * FROM Publications ORDER BY pubID ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getInt("pubID"));
                row.add(rs.getString("title"));
                row.add(rs.getString("topic"));
                row.add(rs.getString("type"));
                row.add(rs.getString("periodicity"));

                data.add(row);
            }

            Util.printTable("Master Publication Registry",
                    List.of("ID", "Title", "Topic", "Type", "Periodicity"),
                    data
            );

        } catch (SQLException e) {
            throw new FailedOperationException("Failed to retrieve publication list: " + e.getMessage());
        }
    }
}
