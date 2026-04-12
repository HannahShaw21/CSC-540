package operations.articles;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class SearchArticlesByAuthor extends Operation {

    public String author;

    public SearchArticlesByAuthor() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}