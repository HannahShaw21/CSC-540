package operations.issues;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class CreateNewIssue extends Operation {

    public int pubID;
    public int articleNum;
    public Optional<String> title = Optional.empty();
    public LocalDate writtenDate;

    public CreateNewIssue() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}