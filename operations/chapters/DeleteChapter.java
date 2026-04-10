package operations.chapters;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;

public class DeleteChapter extends Operation {

    public String isbn;
    public int chapterNum;

    public DeleteChapter() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}