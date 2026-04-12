package operations.articles;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class SearchArticlesByTopic extends Operation {

    public String topic;

    public SearchArticlesByTopic() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}