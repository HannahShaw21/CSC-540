package gutenberg.operations;

import java.util.Arrays;
import java.util.Iterator;

public class OperationGroup implements Iterable<Class> {

    private String groupName;
    private Class[] operations;

    public OperationGroup(String groupName, Class... operations) {
        this.groupName = groupName;

        for (Class c : operations) {
            if (c.getSuperclass() != Operation.class)
                throw new RuntimeException("Tried to add non-operation class '" + c.getSimpleName() + "' to an operation group");
        }

        this.operations = operations;
    }

    public String getName() {
        return groupName;
    }

    public int getOperationCount() {
        return operations.length;
    }

    public Class getOperation(int localID) {
        if (localID < 0 || localID >= operations.length)
            throw new IndexOutOfBoundsException("Tried to access non-existing operation");
        return operations[localID];
    }

    @Override
    public Iterator<Class> iterator() {
        return Arrays.stream(operations).iterator();
    }
}
