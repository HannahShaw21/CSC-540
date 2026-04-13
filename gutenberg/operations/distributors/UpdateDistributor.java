package gutenberg.operations.distributors;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 2.2: Update information for an existing distributor.
 * Allows for partial updates of contact info, address, and financial balances.
 * Also supports renaming the distributor via the 'newName' field.
 */
public class UpdateDistributor extends Operation {

    /** The current name of the distributor (used to find the record). */
    public String name;

    /** Optional new name if the distributor is being renamed. */
    public Optional<String> newName = Optional.empty();
    public Optional<String> type = Optional.empty();
    public Optional<String> contactName = Optional.empty();
    public Optional<String> phoneNum = Optional.empty();
    public Optional<String> streetAddr = Optional.empty();
    public Optional<String> city = Optional.empty();
    public Optional<Float> totalBilled = Optional.empty();
    public Optional<Float> totalPaid = Optional.empty();

    public UpdateDistributor() {}

    /**
     * Dynamically builds the UPDATE statement based on provided Optional fields.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if no update fields are provided.
     */
    @Override
    public void run(Statement stmt) {
        // Guard clause: Ensure there is actually something to update
        if (newName.isEmpty() && type.isEmpty() && contactName.isEmpty() && 
            phoneNum.isEmpty() && streetAddr.isEmpty() && city.isEmpty() && 
            totalBilled.isEmpty() && totalPaid.isEmpty()) {
            throw new FailedOperationException("No update fields provided. Please specify at least one change.");
        }

        StringBuilder sql = new StringBuilder("UPDATE Distributors SET ");
        boolean notFirst = false;

        // Logic for each optional field
        if (newName.isPresent()) {
            sql.append("distributorName = \"").append(newName.get()).append("\"");
            notFirst = true;
        }
        if (type.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("type = \"").append(type.get()).append("\"");
            notFirst = true;
        }
        if (contactName.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("contactPerson = \"").append(contactName.get()).append("\"");
            notFirst = true;
        }
        if (phoneNum.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("phoneNum = \"").append(phoneNum.get()).append("\"");
            notFirst = true;
        }
        if (streetAddr.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("streetAddr = \"").append(streetAddr.get()).append("\"");
            notFirst = true;
        }
        if (city.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("city = \"").append(city.get()).append("\"");
            notFirst = true;
        }
        if (totalBilled.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("totalBilled = ").append(totalBilled.get());
            notFirst = true;
        }
        if (totalPaid.isPresent()) {
            if (notFirst) sql.append(", ");
            sql.append("totalPaid = ").append(totalPaid.get());
        }

        // The WHERE clause must use the ORIGINAL name
        sql.append(" WHERE distributorName = \"").append(name).append("\";");

        try {
            int rowsAffected = stmt.executeUpdate(sql.toString());
            
            if (rowsAffected > 0) {
                String feedback = newName.isPresent() ? name + " renamed to " + newName.get() : name;
                System.out.println("Success: Information updated for " + feedback + ".");
            } else {
                System.out.println("Notice: No distributor found with the name '" + name + "'.");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Update failed: " + e.getMessage());
        }
    }
}
