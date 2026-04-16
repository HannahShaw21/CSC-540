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
 * Task 2.8 (General): List all distribution partners and their current status.
 * Displays contact information and financial balances in a formatted table.
 */
public class ListDistributors extends Operation {

    public ListDistributors() {}

    /**
     * Executes the query and prints a formatted report.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the column names do not match the schema.
     */
    @Override
    public void run(Statement stmt) {
        // SQL targets the 'Distributors' table
        String sql = "SELECT * FROM Distributors ORDER BY name ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getString("name"));
                row.add(rs.getString("type"));
                row.add(rs.getString("contactName"));
                row.add(rs.getString("phoneNum"));
                row.add(rs.getFloat("totalBilled"));
                row.add(rs.getFloat("totalPaid"));

                data.add(row);
            }

            Util.printTable("Distributors",
                    List.of("Name", "Type", "Contact", "Phone #", "Total Billed", "Total Paid"),
                    data
            );

        } catch (SQLException e) {
            // No import needed for FailedOperationException since it's now in the same package
            throw new FailedOperationException("Failed to retrieve distributor list. Error: " + e.getMessage());
        }
    }
}
