package operations.chapters;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.util.Optional;

public class UpdateChapter extends Operation {

    public String isbn;
    public int chapterNum;
    public Optional<String> newIsbn = Optional.empty();
    public Optional<Integer> newChapterNum = Optional.empty();
    public Optional<String> newTitle = Optional.empty();
    public Optional<String> newContent = Optional.empty();

    public UpdateChapter() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}