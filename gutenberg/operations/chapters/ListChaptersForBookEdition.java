package gutenberg.operations.chapters;

import gutenberg.Util;
import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Task 1.6-7: List all chapters for a book edition.
 * Helps when editing content for a book edition.
 */
public class ListChaptersForBookEdition extends Operation {

    public String isbn;

    public ListChaptersForBookEdition() {}

    /**
     * Executes the query and prints a list of all chapters for a book.
     * @param stmt The active JDBC Statement.
     * @throws FailedOperationException if the database query fails.
     */
    @Override
    public void run(Statement stmt) {
        // Query to get everything, ordered by ID for a logical sequence
        String sql = String.format("SELECT * FROM Chapters WHERE isbn = %s ORDER BY chapterNum ASC;",
                Util.sqlStrWrapper(isbn));

        try {
            ResultSet rs = stmt.executeQuery(sql);

            List<List<Object>> data = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rs.getString("isbn"));
                row.add(rs.getInt("chapterNum"));
                row.add(rs.getString("title"));
                row.add(rs.getString("content"));

                data.add(row);
            }

            Util.printTable("Chapters for Book " + isbn,
                    List.of("ISBN", "Chapter #", "Title", "Content Path"),
                    data
            );

        } catch (SQLException e) {
            throw new FailedOperationException("Failed to retrieve publication list: " + e.getMessage());
        }
    }
}
