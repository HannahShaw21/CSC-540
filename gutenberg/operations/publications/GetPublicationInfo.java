package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.Statement;

public class GetPublicationInfo extends Operation {

    public int pubID;

    public GetPublicationInfo() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}