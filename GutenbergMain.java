import operations.FailedOperationException;
import operations.Operation;

import java.lang.reflect.*;
import java.sql.SQLException;

import java.util.*;
import java.io.Console;

public class GutenbergMain {

    private static Scanner in;
    private static GutenbergConnection gbConn;

    public static void main(String[] args) throws SQLException {

        in = new Scanner(System.in);

        // Connect to the database
        try {
            Console console = System.console();

            System.out.print("Welcome to GutenbergPress Database!\nPlease enter your username: ");

            String user;
            String pswd;
            if (console == null) {
                user = in.nextLine();
                System.out.print("Enter your password (WILL BE VISIBLE): ");
                pswd = in.nextLine();
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

        try {
            System.out.println("Connected to database! Use the following commands:\n" +
                    "- QUIT: exits the program\n" +
                    "- LIST: lists all the operations\n" +
                    "- <operationID>: runs the operation associated with the given ID\n");

            // Entering main CLI loop
            while (true)
                if (readConsoleCommand()) break;
        } finally {
            System.out.println("Closing database connection...");
            gbConn.close();
            System.out.println("Connection closed!");
        }

        System.out.println("Bye!");
    }

    /**
     * Reads and processes a console command from the user.
     * @return True if the program should end, False otherwise
     */
    private static boolean readConsoleCommand() {
        System.out.print("> ");
        String input = in.nextLine();

        // basic commands
        if (input.equalsIgnoreCase("quit"))
            return true;
        if (input.equalsIgnoreCase("list")) {
            System.out.println("(Square Brackets indicate an optional parameter)");
            for (int i = 0; i < GutenbergConnection.OPERATIONS.length; i++) {
                System.out.println(i + ": " + GutenbergConnection.getOperationSignature(i));
            }
            return false;
        }

        // Try interpreting it as an operation ID.
        int opID;
        try {
            opID = Integer.parseInt(input);
            if (opID < 0 || opID >= GutenbergConnection.OPERATIONS.length) {
                // Input was not a valid operation ID
                System.out.println("Found no operation with ID " + opID + ".\n" +
                        "Use 'LIST' command to see all available operations");
                return false;
            }

        } catch (NumberFormatException _) {
            // Input was not a number, and wasn't a predefined command
            System.out.println("'" + input + "' is not a valid command.");
            return false;
        }

        System.out.println("Selected operation " + opID + ": " + GutenbergConnection.getOperationSignature(opID));
        Class op = GutenbergConnection.OPERATIONS[opID];
        Constructor opConstructor = op.getConstructors()[0];

        Operation preparedOperation;
        try {
            preparedOperation = (Operation) opConstructor.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException _) {
            System.out.println("Something went wrong processing operation '" + op.getSimpleName() + "'.");
            return false;
        }

        Field[] parameters = op.getFields();

        if (parameters.length > 0) {
            // This operation has some parameters.
            // Read them from the user.

            // Use this set to make sure all required parameters are met
            HashSet<String> requiredParameters = new HashSet<>();
            for (Field p : parameters) {
                if (p.getType() != Optional.class)
                    requiredParameters.add(p.getName());
            }

            // Loop to read in parameter assignments
            while (true) {
                System.out.print("Enter parameter assignment (or leave blank to submit): ");
                String assignment = in.nextLine();

                if (assignment.isBlank()) {
                    // User is trying to submit their operation
                    if (!requiredParameters.isEmpty()) {
                        System.out.print("Missing required parameters: ");
                        System.out.println(String.join(",", requiredParameters));
                        continue; // ask for more parameter assignments
                    }

                    break; // Break out of the assignment loop
                }

                int splitIndex = assignment.indexOf("=");
                if (splitIndex < 0) {
                    System.out.println("Parameter assignment '" + assignment + "' missing equals sign.");
                    continue; // ask for a new parameter assignment
                }

                // Get the name of the parameter being assigned
                String parameterName = assignment.substring(0, splitIndex).strip();
                Field parameter;
                try {
                    parameter = op.getField(parameterName);
                } catch (NoSuchFieldException _) {
                    System.out.println("No such parameter '" + parameterName + "'.");
                    continue; // ask for a new parameter assignment
                }

                // Get the type of the parameter
                Class parameterType = parameter.getType();
                if (parameterType == Optional.class)
                    parameterType = Util.getTypeFromOptionalParameter(parameter);

                // Get the value the user is trying to assign
                String parameterStringedValue = assignment.substring(splitIndex + 1).strip();
                Object castParameter;

                // Attempt to cast value to the proper type
                try {
                    castParameter = Util.parseObjectFromString(parameterStringedValue, parameterType);
                } catch (Util.ParseObjectException e) {
                    System.out.println("Parameter '" + parameterName + "' must be " +
                            parameterType.getSimpleName() + ".");
                    continue; // ask for a new parameter assignment
                }

                // Assign the value to the parameter
                try {
                    if (parameter.getType() == Optional.class)
                        parameter.set(preparedOperation, Optional.of(castParameter));
                    else parameter.set(preparedOperation, castParameter);
                } catch (IllegalAccessException e) {
                    System.out.println("Cannot set parameter '" + parameterName + "'. Must be declared public.");
                    continue; // ask for a new parameter assignment
                }

                // Mark the parameter as included
                requiredParameters.remove(parameterName);
            }

            // Print out the finalized version of the operation
            StringBuilder confirmationCheck = new StringBuilder("Final operation: ").append(op.getSimpleName()).append("(");
            for (int paramID = 0; paramID < parameters.length; paramID++) {
                Field p = parameters[paramID];
                Class pType = p.getType();
                Object pValue;
                try {
                    pValue = p.get(preparedOperation);
                } catch (IllegalAccessException e) {
                    System.out.println("Cannot set parameter '" + p.getName() + "'. Must be declared public.");
                    return false; // cancel operation
                }

                // Optional parameters require a bit more logic
                if (pType == Optional.class) {
                    if (((Optional) pValue).isEmpty())
                        continue; // Empty optional parameter, don't bother displaying
                    else {
                        pType = Util.getTypeFromOptionalParameter(p);
                        pValue = ((Optional) pValue).get();
                    }
                }

                if (paramID != 0) confirmationCheck.append(", ");

                confirmationCheck.append(p.getName()).append("=");
                if (pType == String.class) {
                    confirmationCheck.append("\"").append(pValue).append("\"");
                } else confirmationCheck.append(pValue);
            }
            confirmationCheck.append(")");
            System.out.println(confirmationCheck);
        }

        // Ask for confirmation
        System.out.print("Confirm? (y/n) ");
        String confirmString = in.nextLine();
        if (!confirmString.equalsIgnoreCase("y")) {
            System.out.println("\nOPERATION CANCELLED");
            return false; // They don't want this operation, cancel it
        }

        // Call the operation with the proper parameters
        try {
            System.out.println("\nProcessing operation...");
            gbConn.execute(preparedOperation);
            System.out.println("OPERATION COMPLETE");
        } catch (FailedOperationException e) {
            System.out.println("OPERATION FAILED! " + e.getMessage());
        }

        return false;
    }
}
