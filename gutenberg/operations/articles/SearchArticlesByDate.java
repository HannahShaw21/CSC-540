package gutenberg.operations.articles;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class SearchArticlesByDate extends Operation {

    public LocalDate from;
    public LocalDate to;

    public SearchArticlesByDate() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}