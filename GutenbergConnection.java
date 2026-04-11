import operations.Operation;
import operations.publications.AssignEditorToPublication;
import operations.publications.DeletePublication;
import operations.publications.UpdatePublication;

import java.lang.reflect.Field;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
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
            UpdatePublication.class,
            DeletePublication.class,
            AssignEditorToPublication.class
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
                sb.append("Optional[");
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

    /**
     * Initializes all tables used by the database, resetting them if they already existed.
     * @return True if the reset was successful, False if an error occurred
     */
    public boolean initializeTables() {
        try {
            stmt.execute("START TRANSACTION ");

            stmt.execute("DROP TABLE IF EXISTS IssueOf;");
            stmt.execute("DROP TABLE IF EXISTS EditionOf;");
            stmt.execute("DROP TABLE IF EXISTS Authors;");
            stmt.execute("DROP TABLE IF EXISTS Edits;");
            stmt.execute("DROP TABLE IF EXISTS Orders;");
            stmt.execute("DROP TABLE IF EXISTS Distributors;");
            stmt.execute("DROP TABLE IF EXISTS Payments;");
            stmt.execute("DROP TABLE IF EXISTS Writers;");
            stmt.execute("DROP TABLE IF EXISTS Articles;");
            stmt.execute("DROP TABLE IF EXISTS Issues;");
            stmt.execute("DROP TABLE IF EXISTS Chapters;");
            stmt.execute("DROP TABLE IF EXISTS BookEditions;");
            stmt.execute("DROP TABLE IF EXISTS Publications;");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS Publications (" +
                    "pubID INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(64) NOT NULL," +
                    "topic VARCHAR(256)," +
                    "type VARCHAR(8) NOT NULL " +
                    "CHECK (type IN ('book', 'magazine', 'journal'))," +
                    "periodicity VARCHAR(8)" +
                    "CHECK (periodicity IN (NULL, 'weekly', 'monthly', 'yearly'))" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS BookEditions (" +
                    "isbn VARCHAR(13) PRIMARY KEY," +
                    "editionNum INT NOT NULL CHECK (editionNum > 0)," +
                    "pubDate DATE NOT NULL," +
                    "writtenDate DATE NOT NULL" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Chapters (" +
                    "isbn VARCHAR(13) NOT NULL " +
                    "REFERENCES BookEditions(isbn) ON DELETE CASCADE," +
                    "chapterNum INT NOT NULL CHECK (chapterNum > 0)," +
                    "title VARCHAR(128)," +
                    "content VARCHAR(256) NOT NULL," +
                    "PRIMARY KEY(isbn, chapterNum)" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Issues (" +
                    "issueID INT AUTO_INCREMENT PRIMARY KEY," +
                    "issueNum INT NOT NULL CHECK (issueNum > 0)," +
                    "issueTitle VARCHAR(128)," +
                    "pubDate DATE NOT NULL" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Articles (" +
                    "issueID INT REFERENCES Issues(issueID) ON DELETE CASCADE," +
                    "articleNum INT NOT NULL," +
                    "title VARCHAR(64) NOT NULL," +
                    "topic VARCHAR(256)," +
                    "writtenDate DATE NOT NULL," +
                    "content VARCHAR(256) NOT NULL," +
                    "PRIMARY KEY (issueID, articleNum)" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Writers (" +
                    "writerID INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(64) NOT NULL," +
                    "staffOrInvited VARCHAR(8) NOT NULL " +
                    "CHECK (staffOrInvited IN ('Staff', 'Invited'))" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Payments (" +
                    "paymentID INT AUTO_INCREMENT PRIMARY KEY," +
                    "writerID INT NOT NULL REFERENCES Writers(writerID)," +
                    "amount FLOAT NOT NULL CHECK (amount >= 0)," +
                    "payDate DATE NOT NULL, " +
                    "claimDate DATE DEFAULT NULL," +
                    "reason VARCHAR(256)" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Distributors (" +
                    "name VARCHAR(64) PRIMARY KEY," +
                    "type VARCHAR(64) NOT NULL " +
                    "CHECK (type IN ('bookstore', 'wholesale', 'library'))," +
                    "contactName VARCHAR(64)," +
                    "phoneNum VARCHAR(25) " +
                    "CHECK (phoneNum REGEXP \"^[0-9]{3}-[0-9]{3}-[0-9]{4}\")," +
                    "streetAddr VARCHAR(64) NOT NULL," +
                    "city VARCHAR(64) NOT NULL," +
                    "totalBilled FLOAT CHECK (totalBilled >= 0)," +
                    "totalPaid FLOAT CHECK (totalPaid >= 0)" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Orders (" +
                    "orderID INT AUTO_INCREMENT PRIMARY KEY," +
                    "disName VARCHAR(64) NOT NULL " +
                    "REFERENCES Distributors(name) ON UPDATE CASCADE," +
                    "isbn VARCHAR(13) REFERENCES BookEditions(isbn)," +
                    "issueID INT REFERENCES Issues(issueID)," +
                    "unitPrice FLOAT NOT NULL CHECK (unitPrice >= 0)," +
                    "quantity INT NOT NULL CHECK (quantity >= 0)," +
                    "shippingCost FLOAT NOT NULL CHECK (shippingCost >= 0)," +
                    "orderDate DATE NOT NULL," +
                    "deliveryDate DATE NOT NULL," +
                    "CHECK ( (isbn IS NULL) != (issueID IS NULL) )" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Edits (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "writerID INT NOT NULL " +
                    "REFERENCES Writers(writerID) ON DELETE CASCADE," +
                    "pubID INT NOT NULL " +
                    "REFERENCES Publications(pubID) ON DELETE CASCADE" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS Authors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "writerID INT NOT NULL " +
                    "REFERENCES Writers(writerID) ON DELETE CASCADE," +
                    "isbn VARCHAR(13) REFERENCES BookEditions(isbn)," +
                    "issueID INT," +
                    "articleNum INT," +
                    "FOREIGN KEY (issueID, articleNum) REFERENCES Articles(issueID, articleNum)" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS EditionOf (" +
                    "isbn VARCHAR(13) PRIMARY KEY " +
                    "REFERENCES BookEditions(isbn) ON DELETE CASCADE," +
                    "pubID INT NOT NULL " +
                    "REFERENCES Publications(pubID) ON DELETE CASCADE" +
                    ");"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS IssueOf (" +
                    "issueID INT PRIMARY KEY " +
                    "REFERENCES Issues(issueID) ON DELETE CASCADE," +
                    "pubID INT NOT NULL " +
                    "REFERENCES Publications(pubID) ON DELETE CASCADE" +
                    ");"
            );
            
            stmt.execute("COMMIT");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occured during table setup: " + e.getMessage());
            try {
                // 5. If ANY step failed, undo everything inside this transaction
                stmt.execute("ROLLBACK;");
            } catch (SQLException rollbackEx) {
                // This would only happen if the database connection itself died
                System.err.println("CRITICAL FAILURE: Could not rollback transaction.");
            }
            return false;
        }
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
