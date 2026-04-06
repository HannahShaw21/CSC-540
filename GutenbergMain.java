import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Console;
import java.util.Arrays;

public class GutenbergMain {

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        // Connect to the database
        GutenbergConnection gbConn;
        try {
            Console console = System.console();

            System.out.print("Welcome to GutenbergPress Database!\nPlease enter your username: ");

            String user;
            String pswd;
            if (console == null) {
                user = scanner.nextLine();
                System.out.print("Enter your password (WILL BE VISIBLE): ");
                pswd = scanner.nextLine();
            } else {
                user = console.readLine();
                System.out.print("Enter your password: ");
                pswd = Arrays.toString(console.readPassword());
            }

            gbConn = new GutenbergConnection(user, pswd);
        } catch (SQLException e) {
            System.out.println("Could not connect to database: " + e.getMessage());
            System.exit(1);
            return; // Don't actually need this, but makes the compiler stop complaining about gbConn not being init'd
        }

        // Initialize the database operation list
        GutenbergConnection.initializeOperations();

        System.out.println("Connected to database! Use the following commands:\n" +
                            "- QUIT: exits the program\n" +
                            "- LIST: lists all the operations\n" +
                            "- [operationID]: runs the operation associated with the given ID\n");

        // Entering main CLI loop
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            // basic commands
            if (input.equalsIgnoreCase("quit"))
                break;
            if (input.equalsIgnoreCase("list")) {

                for (int i = 0; i < GutenbergConnection.getNumOperations(); i++) {
                    System.out.println(i + ": " + GutenbergConnection.getOperationSignature(i));
                }
                continue;
            }

            // Try interpreting it as an operation ID.
            try {
                int opID = Integer.parseInt(input);
                if (opID < 0 || opID >= GutenbergConnection.getNumOperations()) {
                    System.out.println("Found no operation with ID " + opID + ".\n" +
                                        "Use 'LIST' command to see all available operations");
                    continue;
                }

                System.out.println("Selected operation " + opID + ": " + GutenbergConnection.getOperationSignature(opID));
                Method op = GutenbergConnection.getOperation(opID);

                // Read all necessary parameters
                Parameter[] neededParameters = op.getParameters();
                Object[] castParameters = new Object[neededParameters.length];
                for (int paramID = 0; paramID < neededParameters.length; paramID++) {
                    Parameter p = neededParameters[paramID];
                    System.out.print("Enter value for " + p.getName() + " (" + p.getType().getSimpleName() + "): ");
                    String paramInput = scanner.nextLine();

                    // Cast parameters to the proper type
                    if (p.getType() == String.class) {
                        castParameters[paramID] = paramInput;
                    } else if (p.getType() == int.class) {
                        // Wrap the parsing in a loop to ensure they input the correct type
                        boolean askAgain;
                        do {
                            askAgain = false;
                            try {
                                castParameters[paramID] = Integer.parseInt(paramInput);
                            } catch (NumberFormatException _) {
                                askAgain = true;
                                System.out.print("'" + paramInput + "' is not an integer. Try again: ");
                                paramInput = scanner.nextLine();
                            }
                        } while (askAgain);
                    } else if (p.getType() == float.class) {
                        // Wrap the parsing in a loop to ensure they input the correct type
                        boolean askAgain;
                        do {
                            askAgain = false;
                            try {
                                castParameters[paramID] = Float.parseFloat(paramInput);
                            } catch (NumberFormatException _) {
                                askAgain = true;
                                System.out.print("'" + paramInput + "' is not an float. Try again: ");
                                paramInput = scanner.nextLine();
                            }
                        } while (askAgain);
                    }
                }

                // Confirm the operation
                if (neededParameters.length == 0) System.out.print("Confirm? (y/n) ");
                else {
                    System.out.print("Final operation: " + op.getName() + "(");
                    for (int paramID = 0; paramID < neededParameters.length; paramID++) {
                        if (paramID != 0) System.out.print(", ");

                        Parameter p = neededParameters[paramID];
                        System.out.print(p.getName() + "=");
                        if (p.getType() == String.class) {
                            System.out.print("\"" + castParameters[paramID] + "\"");
                        } else System.out.print(castParameters[paramID]);
                    }
                    System.out.print(")\nConfirm? (y/n) ");
                }

                String confirmString = scanner.nextLine();
                if (!confirmString.equalsIgnoreCase("y"))
                    continue; // They don't want this operation. back to top of loop.

                // Call method with the proper methods
                try {
                    op.invoke(gbConn, castParameters);
                } catch (IllegalAccessException | InvocationTargetException _) {}

            } catch (NumberFormatException _) {
                System.out.println("'" + input + "' is not a valid command.");
            }
        }

        gbConn.close();
        System.out.println("Bye!");
    }
}