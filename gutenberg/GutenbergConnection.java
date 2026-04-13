package gutenberg;

import gutenberg.operations.Operation;
import gutenberg.operations.publications.AssignEditorToPublication;
import gutenberg.operations.publications.CreateNewPublication;
import gutenberg.operations.publications.DeletePublication;
import gutenberg.operations.publications.UpdatePublication;

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
            CreateNewPublication.class,
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
    public boolean rebuildDatabase() {
        try {
            stmt.execute("START TRANSACTION;");

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
                    "CHECK (periodicity IN (NULL, 'weekly', 'monthly', 'yearly')), " +
                    "CONSTRAINT TypePeriodicityEnforce CHECK (type = 'book' OR periodicity IS NOT NULL)" +
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

            stmt.execute("COMMIT;");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occurred during table setup: " + e.getMessage());
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

    public boolean resetToDummyData() {
        // First rebuild the database (clears existing data)
        if (!rebuildDatabase()) {
            System.out.println("Could not reset data: failed to rebuild database");
            return false;
        }

        try {
            stmt.execute("START TRANSACTION;");

            stmt.executeUpdate("INSERT INTO Publications (pubID, title, topic, type, periodicity) VALUES " +
                    "(6, \"High School Astronomy\", \"educational;space;science\", \"book\", NULL), " +
                    "(1, \"Dune\", \"fiction;sci-fi;space\", \"book\", NULL), " +
                    "(2, \"MAD\", \"comedy\", \"magazine\", \"monthly\"), " +
                    "(3, \"TIME\", \"politics;news\", \"magazine\", \"weekly\"), " +
                    "(4, \"National Science Review, Volume 4\", \"science\", \"journal\", \"monthly\");"
            );
            stmt.executeUpdate("INSERT INTO BookEditions (isbn, editionNum, pubDate, writtenDate) VALUES " +
                    "(\"anisbn1\", 1, \"2010-02-23\", \"2010-01-15\"), " +
                    "(\"anisbn2\", 2, \"2011-03-20\", \"2010-08-12\"), " +
                    "(\"anisbn3\", 1, \"1965-10-01\", \"1965-10-1\"), " +
                    "(\"anisbn4\", 3, \"2012-03-19\", \"2012-03-02\");"
            );
            stmt.executeUpdate("INSERT INTO Chapters (isbn, chapterNum, title, content) VALUES " +
                    "(\"anisbn1\", 1, \"Planets are really cool\", \"/a/file/path/astro1\"), " +
                    "(\"anisbn1\", 2, \"Planets are also really big\", \"/a/file/path/astro2\"), " +
                    "(\"anisbn2\", 1, \"Planets are really cool\", \"a/file/path/astro11.5\"), " +
                    "(\"anisbn2\", 2, \"Planets are also really big\", \"/a/file/path/astro2\"), " +
                    "(\"anisbn2\", 3, \"Pluto isn't a planet btw\", \"/a/file/path/astro3\"), " +
                    "(\"anisbn3\", 10, NULL, \"/a/file/path/dune10\");"
            );
            stmt.executeUpdate("INSERT INTO Issues (issueID, issueNum, issueTitle, pubDate) VALUES \n" +
                    "(9, 44, \"MAD...of STEAL\", \"2025-06-25\"),\n" +
                    "(1, 45, \"MAD Science\", \"2025-10-01\"),\n" +
                    "(2, 1, NULL, \"2017-01-01\"),\n" +
                    "(3, 4, NULL, \"2017-07-01\");"
            );
            stmt.executeUpdate("INSERT INTO Articles (issueID, articleNum, title, topic, writtenDate, content) VALUES  " +
                    "(9, 1, \"Spy vs. Spy\", \"comedy;spys\", \"2025-04-13\", \"/articles/1\"), " +
                    "(9, 6, \"Superduperman (A MAD Movie Parody)\", " +
                    "\"comedy;superheroes\", \"2025-04-09\", \"/articles/2\"), " +
                    "(1, 3, \"Spy vs. Spy\", \"comedy;spys\", \"2025-09-20\", \"/articles/3\"), " +
                    "(1, 11, \"The Horror of Quack-enstein\", \"comedy;horror\", \"2025-09-25\", \"/articles/4\"), " +
                    "(3, 6, \"Deformation and fracture mechanisms of nanotwinned metals\", " +
                    "\"science;metalurgy\", \"2017-04-30\", \"/articles/4\");"
            );
            stmt.executeUpdate("INSERT INTO Writers (writerID, name, staffOrInvited) VALUES  " +
                    "(1, \"Rithvik Srinivas\", \"Staff\"), " +
                    "(2, \"Hannah Shaw\", \"Staff\"), " +
                    "(3, \"Tyler Johnson\", \"Staff\"), " +
                    "(4, \"Frank Herbert\", \"Invited\"), " +
                    "(5, \"Neil DeGrase Tyson\", \"Invited\"), " +
                    "(6, \"Stephen King\", \"Invited\");"
            );
            stmt.executeUpdate("INSERT INTO Payments(paymentID, writerID, amount, payDate, claimDate, reason) " +
                    "VALUES " +
                    "(1, 1, 500.00, '2026-03-01', '2026-03-02', 'Editorial work for High School Astronomy'), " +
                    "(2, 4, 1200.50, '2026-03-05', '2026-03-05', 'Book commission for Dune'), " +
                    "(3, 5, 75.00, '2026-03-08', '2026-03-09', 'Article authorship: Space Exploration'), " +
                    "(4, 3, 80.00, \"2026-03-11\", \"2026-03-11\", \"he just works here\"); "
            );
            stmt.executeUpdate("INSERT INTO Distributors (name, type, contactName, phoneNum, streetAddr, city, totalBilled, totalPaid) VALUES  " +
                    "(\"NCSU Bookstore\", \"bookstore\", \"Tuffy Tiger\", \"919-515-2161\", \"2521 Main Campus Dr\", \"Raleigh\", 5000.00, 4500.00), " +
                    "(\"Wake County Public Library\", \"library\", \"Jane Doe\", \"919-856-6710\", \"401 Fayetteville St\", \"Raleigh\", 1200.50, 1200.50), " +
                    "(\"Ingram Content Group\", \"wholesale\", \"John Smith\", \"615-793-5000\", \"1 Ingram Blvd\", \"La Vergne\", 25000.00, 20000.00), " +
                    "(\"Quail Ridge Books\", \"bookstore\", \"Sarah Miller\", \"919-828-1588\", \"4209 Lassiter Mill Rd\", \"Raleigh\", 3200.75, 3200.75);"
            );
            stmt.executeUpdate("INSERT INTO Orders (orderID, disName, isbn, issueID, unitPrice, quantity, shippingCost, orderDate, deliveryDate) VALUES  " +
                    "(5, 'NCSU BookStore', 'anisbn1', NULL, 45.00, 50, 15.00, \"2026-03-01\", '2026-03-01'), " +
                    "(6, 'Wake County Public Library', 'anisbn3', NULL, 20.00, 10, 5.00, '2026-03-02','2026-03-07'), " +
                    "(7, \"Ingram Content Group\", NULL, 9, 5.50, 200, 50.00, \"2026-03-4\", \"2026-03-12\"), " +
                    "(8, \"Quail Ridge Books\", NULL, 3, 12.00, 15, 8.50, \"2026-03-05\",\"2026-03-15\");"
            );
            stmt.executeUpdate("INSERT INTO Edits (id, writerID, pubID) VALUES " +
                    "(1, 1, 6), " +
                    "(2, 2, 2), " +
                    "(3, 4, 4), " +
                    "(4, 3, 1);"
            );
            stmt.executeUpdate("INSERT INTO Authors (id, writerID, isbn, issueID, articleNum) VALUES " +
                    "(1, 5, \"anisbn1\", NULL, NULL), " +
                    "(2, 4, \"anisbn3\", NULL, NULL), " +
                    "(3, 1, NULL, 9, 1), " +
                    "(5, 6, \"anisbn4\", NULL, NULL);"
            );
            stmt.executeUpdate("INSERT INTO EditionOf (isbn, pubID) VALUES " +
                    "(\"anisbn1\", 6), " +
                    "(\"anisbn2\", 6), " +
                    "(\"anisbn3\", 1), " +
                    "(\"anisbn4\", 6);"
            );
            stmt.executeUpdate("INSERT INTO IssueOf (issueID, pubID) VALUES " +
                    "(9, 2), " +
                    "(1, 2), " +
                    "(2, 4), " +
                    "(3, 4);"
            );
            
            stmt.execute("COMMIT;");
            return true;
        } catch (SQLException e) {
            System.out.println("Error occurred during data repopulation: " + e.getMessage());
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
