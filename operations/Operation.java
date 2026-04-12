package operations;

import java.sql.Statement;

public abstract class Operation {

    public abstract void run(Statement stmt);

}
