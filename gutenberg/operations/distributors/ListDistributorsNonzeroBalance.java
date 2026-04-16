package gutenberg.operations.distributors;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Task 3.4: List all distributors with a non-zero balance.
 * A non-zero balance indicates that 'totalBilled' does not equal 'totalPaid'.
 */
public class ListDistributorsNonzeroBalance extends Operation {

    public ListDistributorsNonzeroBalance() {}

    /**
     * Executes the query and displays the debt/credit status for each distributor.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Query targets distributors where the accounts haven't settled to zero
        String sql = "SELECT name, totalBilled, totalPaid, (totalPaid - totalBilled) AS balance " +
                     "FROM Distributors " +
                     "WHERE totalBilled != totalPaid " +
                     "ORDER BY balance ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getString("name"));
                row.add(rs.getFloat("totalBilled"));
                row.add(rs.getFloat("totalPaid"));
                row.add(rs.getFloat("balance"));

                data.add(row);
            }

            Util.printTable("Distributors with Nonzero Balance",
                    List.of("Name", "Total Billed", "Total Paid", "Balance"),
                    data
            );
        } catch (SQLException e) {
            // No import needed for FailedOperationException if it's in this folder!
            throw new FailedOperationException("Failed to retrieve balance report: " + e.getMessage());
        }
    }
}
