package operations.publications;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class RemoveEditorsFromPublication extends Operation {

    public int writerID;
    public int pubID;

    public RemoveEditorsFromPublication() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}