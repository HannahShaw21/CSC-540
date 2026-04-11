package operations.issues;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class DeleteIssue extends Operation {

    public int issueID;

    public DeleteIssue() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}