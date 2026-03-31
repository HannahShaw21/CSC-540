import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class GutenburgMain {

    public static void main(String[] args) {

        Method[] operations = GutenburgConnection.class.getDeclaredMethods();

        for (int i = 0; i < operations.length; i++) {
            System.out.print(i + ": " + operations[i].getName() + "(");
            for (Parameter p : operations[i].getParameters())
                System.out.print(" " + p.getType().getSimpleName() + " " + p.getName());
            System.out.println(")");
        }

    }

}