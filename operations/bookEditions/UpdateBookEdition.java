package operations.bookEditions;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class UpdateBookEdition extends Operation {

    public String isbn;
    public Optional<String> newIsbn = Optional.empty();
    public Optional<Integer> newEditionNum = Optional.empty();
    public Optional<LocalDate> newWrittenDate = Optional.empty();
    public Optional<LocalDate> newPubDate = Optional.empty();

    public UpdateBookEdition() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}