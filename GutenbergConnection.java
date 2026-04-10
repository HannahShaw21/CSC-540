import operations.Operation;
import operations.UpdatePublication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.lang.reflect.ParameterizedType;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * A class representing a connection to the Gutenberg Press database.
 * Contains definitions for all the operations a user can make on the database.
 * These signatures of these operations are made statically available so that they
 * can be used to facilitate a generalized user interface.
 */
public class GutenbergConnection {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/";

    private Connection con;
    private Statement stmt;

    public static final Class[] OPERATIONS = new Class[]{
            UpdatePublication.class
    };

    /**
     * Returns a String representing the method signature of the specified operation.
     * @param id the ID of the desired operation
     * @return a String containing the operation's method signature
     *
     * @throws IndexOutOfBoundsException if no operation with the given ID exists
     */
    public static String getOperationSignature(int id) {
        Class op = OPERATIONS[id];
        StringBuilder sb = new StringBuilder();

        sb.append(op.getSimpleName()).append("(");
        Field[] parameters = op.getFields();
        for (Field p : parameters) {
            if (p != parameters[0]) sb.append(", ");

            if (p.getType() == Optional.class)  {
                sb.append("[");
                sb.append(Util.getTypeFromOptionalParameter(p).getSimpleName());
                sb.append(" ").append(p.getName());
                sb.append("]");
            } else sb.append(p.getType().getSimpleName()).append(" ").append(p.getName());
        }
        sb.append(")");

        return sb.toString();
    }


    /**
     * Creates and initializes a connection to the Gutenberg database using the given credentials.
     * @param user the user to connect as
     * @param pswd the password for the user
     * @throws SQLException if the connection fails. More details in the exception message
     */
    public GutenbergConnection(String user, String pswd) throws SQLException {
        // connect to database and populate con and stmt field

        con = DriverManager.getConnection(jdbcURL + user, user, pswd);
        stmt = con.createStatement();
    }

    public void execute(Operation op) {
        op.run(stmt);
    }

    /**
     * Closes the database connection.
     * @throws SQLException if a database access error occurs
     */
    public void close() throws SQLException {
        con.close();
    }
}
