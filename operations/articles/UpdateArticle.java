package operations.articles;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class UpdateArticle extends Operation {

    public int issueID;
    public int articleNum;
    public Optional<Integer> newIssueID = Optional.empty();
    public Optional<Integer> newArticleNum = Optional.empty();
    public Optional<String> newTitle = Optional.empty();
    public Optional<LocalDate> newWrittenDate = Optional.empty();
    public Optional<String> newContent = Optional.empty();

    public UpdateArticle() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}