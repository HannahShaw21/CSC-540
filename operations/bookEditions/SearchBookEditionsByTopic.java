package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class SearchBookEditionsByTopic extends Operation {

    public String topic;

    public SearchBookEditionsByTopic() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}