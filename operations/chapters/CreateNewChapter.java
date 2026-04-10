package operations.chapters;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class CreateNewChapter extends Operation {

    public String isbn;
    public int chapterNum;
    public Optional<String> title = Optional.empty();
    public String content;

    public CreateNewChapter() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}