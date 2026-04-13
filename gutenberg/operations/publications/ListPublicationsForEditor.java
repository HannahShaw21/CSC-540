package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Task 2.8 (Editors): List all publications assigned to a specific editor.
 * <p>
 * This operation joins the 'Publications' and 'Edits' tables to provide
 * a readable report for a given writerID.
 * </p>
 */
public class ListPublicationsForEditor extends Operation {

    /** The ID of the editor/writer to filter by. */
    public int writerID;

    public ListPublicationsForEditor() {}

    /**
     * Executes the JOIN query and displays the results in a table.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if a database error occurs.
     */
    @Override
    public void run(Statement stmt) {
        // SQL JOIN to get title/type details for the specific editor
        String sql = "SELECT p.pubID, p.title, p.topic, p.type " +
                     "FROM Publications p " +
                     "JOIN Edits e ON p.pubID = e.pubID " +
                     "WHERE e.writerID = " + writerID + " " +
                     "ORDER BY p.pubID ASC;";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n--- Editorial Assignments for Writer ID: " + writerID + " ---");
            System.out.printf("%-5s | %-25s | %-15s | %-12s\n", 
                              "ID", "Title", "Topic", "Type");
            System.out.println("-------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d | %-25s | %-15s | %-12s\n", 
                    rs.getInt("pubID"), 
                    rs.getString("title"), 
                    rs.getString("topic"), 
                    rs.getString("type"));
            }

            if (!found) {
                System.out.println("No publications are currently assigned to this editor.");
            }
            System.out.println("-------------------------------------------------------------------\n");

        } catch (SQLException e) {
            throw new FailedOperationException("Failed to retrieve editor's publication list: " + e.getMessage());
        }
    }
}
