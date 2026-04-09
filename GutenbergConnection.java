import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;

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

    private static Method[] OPERATIONS = new Method[0];

    /**
     * Initializes the list of signatures of the operations available to use on the connection.
     * Must be called before any operations can be dynamically accessed.
     *
     * @throws RuntimeException if it attempts to add a non-existent operation
     */
    public static void initializeOperations() {
        try {
            OPERATIONS = new Method[]{
                    GutenbergConnection.class.getMethod("InsertNewPublication", String.class, int.class),
                    GutenbergConnection.class.getMethod("DeletePublication"),
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Tried to reference non-existent operation: " + e.getMessage());
        }
    }

    /**
     * Returns the number of operations in the static operation list.
     * @return the number of available operations
     */
    public static int getNumOperations() {
        return OPERATIONS.length;
    }

    /**
     * Returns the operation with the given ID.
     * @param id the ID of the desired operation
     * @return the Method object associated with the operation
     *
     * @throws IndexOutOfBoundsException if no operation with the given ID exists
     */
    public static Method getOperation(int id) {
        if (id < 0 || id >= OPERATIONS.length)
            throw new IndexOutOfBoundsException("No operation found with the ID '" + id + "'");
        return OPERATIONS[id];
    }

    /**
     * Returns a String representing the method signature of the specified operation.
     * @param id the ID of the desired operation
     * @return a String containing the operation's method signature
     *
     * @throws IndexOutOfBoundsException if no operation with the given ID exists
     */
    public static String getOperationSignature(int id) {
        Method op = getOperation(id);
        StringBuilder sb = new StringBuilder();

        sb.append(op.getName()).append("(");
        for (Parameter p : op.getParameters()) {
            if (p == op.getParameters()[0])
                sb.append(p.getType().getSimpleName()).append(" ").append(p.getName());
            else sb.append(", ").append(p.getType().getSimpleName()).append(" ").append(p.getName());
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

    /**
     * Closes the database connection.
     * @throws SQLException if a database access error occurs
     */
    public void close() throws SQLException {
        con.close();
    }

    /*//////////////////////////////////
    /// TASK 1: Editing & Publishing ///
    //////////////////////////////////*/

    public int CreateNewPublication(String title, String topic, String type) {
        return 0; // should return the pubID of newly created publication
    }

    public void DeletePublication(int pubID) {}

    public void UpdatePublication(int pubID, String title, String topic, String periodicity) {}

    public void AssignEditorToPublication(int writerID, int pubID) {}

    public void RemoveEditorFromPublication(int writerID, int pubID) {}

    public void ListPublicationsForEditor(int writerID) {}

    public void CreateNewArticle(int issueID, int articleNum, String title, LocalDate writtenDate, String content) {}

    public void CreateNewChapter(String isbn, int chapterNum, String title, String content) {}

    public void DeleteArticle(int issueID, int articleNum) {}

    public void DeleteChapter(String isbn, int chapterNum) {}


    /*////////////////////////
    /// TASK 2: Production ///
    ////////////////////// /*/

}