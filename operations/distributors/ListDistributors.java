package operations.distributors;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ListDistributors extends Operation {

    public ListDistributors() {
        
    }

    @Override
    public void run(Statement stmt) {
       try {
            String sql = "SELECT * FROM distributors;";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()){
                String nameValue = rs.getString("name");
                String typeValue = rs.getString("type");
                String contactName = rs.getString("contactName");
                String phoneNum = rs.getString("phoneNum");
                String streetAddr = rs.getString("streetAddr");
                String cityValue = rs.getString("city");
                Float totalBilled = rs.getFloat("totalBilled");
                Float totalPaid = rs.getFloat("totalPaid");
                System.out.println("name: " + nameValue + " type: " + typeValue + " contactName: " + contactName + " phoneNum: " + phoneNum + " streetAddr: " + streetAddr + " city: " + cityValue + " totalBilled: " + totalBilled + " totalPaid: " + totalPaid);
            }

        } catch (SQLException e) {
            // Throw custom exception to handle the error gracefully in the CLI
            throw new FailedOperationException("Distributor(s) not found.");
        }
    }
}
