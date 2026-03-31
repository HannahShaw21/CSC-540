import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GutenburgConnection {

    private Connection con;
    private Statement stmt;

    public GutenburgConnection(String dbName, String pswd) {
        // connect to database and populate con and stmt fields
    }

    public void InsertNewPublication(String title) {
        System.out.println(title);
    }

    public void DeletePublication() {}
}