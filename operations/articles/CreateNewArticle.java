package operations.articles;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class CreateNewArticle extends Operation {

    public int issueID;
    public int articleNum;
    public String title;
    public LocalDate writtenDate;
    public String content;

    public CreateNewArticle() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}