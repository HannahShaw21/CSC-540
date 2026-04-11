package operations.issues;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class CompareIssues extends Operation {

    public int firstIssueID;
    public int secondIssueID;

    public CompareIssues() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}