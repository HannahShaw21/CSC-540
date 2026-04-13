package gutenberg.operations.publications;
import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class CreateNewPublication extends Operation {

    public String title;
    public String topic;
    public String type;
    public Optional<String> periodicity = Optional.empty();

    public CreateNewPublication() {}

    @Override
    public void run(Statement stmt) {
        String sqlCreation = String.format("INSERT INTO Publications(title, topic, type, periodicity) VALUES (%s, %s, %s, %s);",
                        Util.sqlStrWrapper(title),
                        Util.sqlStrWrapper(topic),
                        Util.sqlStrWrapper(type),
                        Util.sqlStrWrapper(periodicity)
        );

        try {
            stmt.executeUpdate(sqlCreation);
            stmt.execute("SELECT LAST_INSERT_ID();");

            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                int pubID = rs.getInt(1);

                System.out.print("Success: Added new publication (pubID = ");
                System.out.print(pubID);
                System.out.println(")");
            } else {
                throw new FailedOperationException("Something went wrong: Couldn't access new publication's pubID");
            }
        } catch (SQLException e) {
            throw new FailedOperationException("Could not create new publication: " + e.getMessage());
        }
    }
}