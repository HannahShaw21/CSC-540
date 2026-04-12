package operations.publications;
import operations.*;

import java.sql.Statement;
import java.util.Optional;

public class CreateNewPublication extends Operation {

    public String title;
    public String topic;
    public Optional<String> periodicity = Optional.empty();

    public CreateNewPublication() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}