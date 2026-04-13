package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Task 1.2: Enter a new distributor.
 * Registers a distribution partner with their contact information 
 * and initial billing status.
 */
public class CreateNewDistributor extends Operation {

    public String name;
    public String type;
    public Optional<String> contactName = Optional.empty();
    public Optional<String> phoneNum = Optional.empty();
    public String streetAddr;
    public String city;
    public Optional<Float> totalBilled = Optional.empty();
    public Optional<Float> totalPaid = Optional.empty();

    public CreateNewDistributor() {}

    /**
     * Executes the insertion of a new distributor.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the distributor already exists 
     * or a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // Helper logic to handle Optional Strings following Tyler's Double Quote Rule
        String contactVal = contactName.isPresent() ? "\"" + contactName.get() + "\"" : "NULL";
        String phoneVal = phoneNum.isPresent() ? "\"" + phoneNum.get() + "\"" : "NULL";
        
        // Helper logic for Optional Floats (no quotes needed for numbers)
        String billedVal = totalBilled.isPresent() ? String.valueOf(totalBilled.get()) : "0.0";
        String paidVal = totalPaid.isPresent() ? String.valueOf(totalPaid.get()) : "0.0";

        // Main SQL construction
        String sql = "INSERT INTO Distributors (distributorName, type, contactPerson, phoneNum, streetAddr, city, totalBilled, totalPaid) VALUES (" +
                     "\"" + name + "\", " + 
                     "\"" + type + "\", " + 
                     contactVal + ", " + 
                     phoneVal + ", " + 
                     "\"" + streetAddr + "\", " + 
                     "\"" + city + "\", " + 
                     billedVal + ", " + 
                     paidVal + ");";

        try {
            stmt.executeUpdate(sql);
            System.out.println("Success: Distributor '" + name + "' added to the system.");
        } catch (SQLException e) {
            // Catches things like duplicate primary keys (distributor names)
            throw new FailedOperationException("Failed to register distributor: " + e.getMessage());
        }
    }
}
