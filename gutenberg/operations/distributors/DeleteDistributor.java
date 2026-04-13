package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;

public class DeleteDistributor extends Operation {

    public String name;

    public DeleteDistributor() {}

    @Override
    public void run(Statement stmt) {
        String sql = "DELETE FROM distributors WHERE name = \"" + name + "\";";

        try {
            int rowsAffected = stmt.executeUpdate(sql);
            
            if (rowsAffected > 0) {
                System.out.println("Success: Distributor '" + name + "' has been deleted.");
            } else {
                // If rowsAffected is 0, the name didn't exist in the table
                System.out.println("Notice: No distributor was found with name '" + name + "'. Nothing was deleted.");
            }
        } catch (SQLException e) {
            // Throw custom exception to handle the error gracefully in the CLI
            throw new FailedOperationException("Failed to delete distributor: " + e.getMessage());
        }
    }
}
