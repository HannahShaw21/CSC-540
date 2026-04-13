package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.Statement;

public class SearchArticlesByTopic extends Operation {

    public String topic;

    public SearchArticlesByTopic() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}