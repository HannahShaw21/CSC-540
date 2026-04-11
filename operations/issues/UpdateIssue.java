package operations.issues;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class UpdateIssue extends Operation {

    public int issueID;
    public Optional<Integer> newIssueNum = Optional.empty();
    public Optional<String> newTitle = Optional.empty();
    public Optional<LocalDate> newPubDate = Optional.empty();

    public UpdateIssue() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}