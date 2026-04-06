import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GutenbergConnection {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/";

    private Connection con;
    private Statement stmt;

    private static Method[] OPERATIONS = new Method[0];

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

    public static int getNumOperations() {
        return OPERATIONS.length;
    }

    public static Method getOperation(int id) {
        if (id < 0 || id >= OPERATIONS.length)
            throw new IndexOutOfBoundsException("No operation found with the ID '" + id + "'");
        return OPERATIONS[id];
    }

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


    public GutenbergConnection(String user, String pswd) throws SQLException {
        // connect to database and populate con and stmt field

        con = DriverManager.getConnection(jdbcURL + user, user, pswd);
        stmt = con.createStatement();
    }

    public void close() throws SQLException {
        con.close();
    }

    public void InsertNewPublication(String title, int pubID) {
        System.out.println(title + " (" + pubID + ")");
    }

    public void DeletePublication() {}
}