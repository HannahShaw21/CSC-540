package operations.publications;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class ListPublicationsForEditor extends Operation {

    public int writerID;

    public ListPublicationsForEditor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}