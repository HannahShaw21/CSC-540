package gutenberg.operations.distributors;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Task 2.7 (Distributors): Search for distributors based on type and/or location.
 * Uses optional filters to narrow down the distributor directory.
 */
public class SearchDistributors extends Operation {

    /** Optional filter for the distributor type (e.g., 'Wholesale', 'Retail'). */
    public Optional<String> type = Optional.empty();
    
    /** Optional filter for the city where the distributor is located. */
    public Optional<String> city = Optional.empty();

    public SearchDistributors() {}

    /**
     * Executes a dynamic search query and prints the results in a formatted table.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Base query
        StringBuilder sql = new StringBuilder("SELECT * FROM Distributors WHERE 1=1");

        // Dynamically add filters based on user input
        if (type.isPresent()) {
            sql.append(" AND type = \"").append(type.get()).append("\"");
        }
        if (city.isPresent()) {
            sql.append(" AND city = \"").append(city.get()).append("\"");
        }

        sql.append(" ORDER BY distributorName ASC;");

        try {
            ResultSet rs = stmt.executeQuery(sql.toString());

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getString("name"));
                row.add(rs.getString("type"));
                row.add(rs.getString("contactName"));
                row.add(rs.getString("phoneNum"));
                row.add(rs.getString("city"));
                row.add(rs.getFloat("totalBilled"));
                row.add(rs.getFloat("totalPaid"));

                data.add(row);
            }

            Util.printTable("Searched Distributors",
                    List.of("Name", "Type", "Contact", "Phone #", "City", "Total Billed", "Total Paid"),
                    data
            );

        } catch (SQLException e) {
            throw new FailedOperationException("Search failed: " + e.getMessage());
        }
    }
}
