package operations.writers;

import operations.FailedOperationException;
import operations.Operation;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class EnterPaymentForWriter extends Operation {

    public int writerID;
    public float amount;
    public LocalDate payDate;
    public Optional<String> reason = Optional.empty();

    public EnterPaymentForWriter() {}

    @Override
    public void run(Statement stmt) {
        throw new FailedOperationException("Operation not implemented");
    }
}