package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class CreateNewBookEdition extends Operation {

    public int pubID;
    public String isbn;
    public int editionNum;
    public LocalDate writtenDate;
    public LocalDate pubDate;

    public CreateNewBookEdition() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}