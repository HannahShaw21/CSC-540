package gutenberg.operations.publications;

import gutenberg.operations.FailedOperationException;
import gutenberg.operations.Operation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UpdatePublication extends Operation {

    public int pubID;
    public Optional<String> title = Optional.empty();
    public Optional<String> topic = Optional.empty();
    public Optional<String> periodicity = Optional.empty();

    public UpdatePublication() {}

    @Override
    public void run(Statement stmt) {
        if (title.isEmpty() && topic.isEmpty() && periodicity.isEmpty())
            throw new FailedOperationException("Please specify something to change about the publication.");

        StringBuilder sqlUpdate = new StringBuilder("UPDATE Publications SET");
        boolean notFirst = false;
        if (title.isPresent()) {
            sqlUpdate.append(" title = \"").append(title.get()).append("\"");
            notFirst = true;
        }
        if (topic.isPresent()) {
            if (notFirst) sqlUpdate.append(",");
            sqlUpdate.append(" topic = \"").append(topic.get()).append("\"");
            notFirst = true;
        }
        if (periodicity.isPresent()) {
            if (notFirst) sqlUpdate.append(",");
            sqlUpdate.append(" periodicity = \"").append(periodicity.get()).append("\"");
            notFirst = true;
        }
        sqlUpdate.append(" WHERE pubID = ").append(pubID).append(";");

        try {
            stmt.executeUpdate(sqlUpdate.toString());
            System.out.println("Publication updated!");
        } catch (SQLException e) {
            throw new FailedOperationException(e.getMessage());
        }
    }
}