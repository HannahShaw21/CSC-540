package operations.articles;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;

public class DeleteArticle extends Operation {

    public int issueID;
    public int articleNum;

    public DeleteArticle() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}