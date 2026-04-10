import operations.FailedOperationException;
import operations.Operation;

import java.lang.reflect.*;
import java.sql.SQLException;

import java.util.*;
import java.io.Console;

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

        System.out.println("Connected to database! Use the following commands:\n" +
                            "- QUIT: exits the program\n" +
                            "- LIST: lists all the operations\n" +
                            "- <operationID>: runs the operation associated with the given ID\n");

        // Entering main CLI loop
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            // basic commands
            if (input.equalsIgnoreCase("quit"))
                break;
            if (input.equalsIgnoreCase("list")) {
                System.out.println("(Square Brackets indicate an optional parameter)");
                for (int i = 0; i < GutenbergConnection.OPERATIONS.length; i++) {
                    System.out.println(i + ": " + GutenbergConnection.getOperationSignature(i));
                }
                continue;
            }

            // Try interpreting it as an operation ID.
            try {
                int opID = Integer.parseInt(input);
                if (opID < 0 || opID >= GutenbergConnection.OPERATIONS.length) {
                    System.out.println("Found no operation with ID " + opID + ".\n" +
                                        "Use 'LIST' command to see all available operations");
                    continue;
                }

                System.out.println("Selected operation " + opID + ": " + GutenbergConnection.getOperationSignature(opID));
                Class op = GutenbergConnection.OPERATIONS[opID];
                Constructor opConstructor = op.getConstructors()[0];

                try {
                    Operation preparedOperation = (Operation) opConstructor.newInstance();
                    Field[] parameters = op.getFields();


                    if (parameters.length > 0) {
                        HashSet<String> requiredParameters = new HashSet<>();
                        for (Field p : parameters) {
                            if (p.getType() != Optional.class)
                                requiredParameters.add(p.getName());
                        }

                        boolean failedAssignment = false;
                        while (true) {
                            System.out.print("Enter parameter assignment (or leave blank to submit): ");
                            String assignment = scanner.nextLine();

                            if (assignment.isBlank()) {

                                if (!requiredParameters.isEmpty()) {
                                    System.out.print("Missing required parameters: ");
                                    System.out.println(String.join(",", requiredParameters));
                                    continue;
                                }

                                break; // Break out of the assignment loop
                            }

                            int splitIndex = assignment.indexOf("=");
                            if (splitIndex < 0) {
                                System.out.println("Parameter assignment '" + assignment + "' missing equals sign.");
                                failedAssignment = true;
                                break;
                            }

                            String parameterName = assignment.substring(0, splitIndex).strip();
                            Field parameter;
                            try {
                                parameter = op.getField(parameterName);
                            } catch (NoSuchFieldException _) {
                                System.out.println("No such parameter '" + parameterName + "'.");
                                failedAssignment = true;
                                break;
                            }

                            Class parameterType = parameter.getType();
                            if (parameterType == Optional.class)
                                parameterType = Util.getTypeFromOptionalParameter(parameter);

                            String parameterStringedValue = assignment.substring(splitIndex + 1).strip();
                            Object castParameter;

                            // Cast parameters to the proper type
                            if (parameterType == String.class) {
                                castParameter = parameterStringedValue;
                            } else if (parameterType == int.class) {
                                try {
                                    castParameter = Integer.parseInt(parameterStringedValue);
                                } catch (NumberFormatException _) {
                                    System.out.print("Parameter '" + parameterName + "' must be an integer.");
                                    failedAssignment = true;
                                    break;
                                }
                            } else if (parameterType == float.class) {
                                try {
                                    castParameter = Integer.parseInt(parameterStringedValue);
                                } catch (NumberFormatException _) {
                                    System.out.print("Parameter '" + parameterName + "' must be a float.");
                                    failedAssignment = true;
                                    break;
                                }
                            } else {
                                throw new RuntimeException("Operation '" + op.getSimpleName() +
                                        "' used unparseable parameter type '" + parameterType.getSimpleName() + "'.");
                            }

                            if (parameter.getType() == Optional.class)
                                parameter.set(preparedOperation, Optional.of(castParameter));
                            else parameter.set(preparedOperation, castParameter);

                            requiredParameters.remove(parameterName);
                        }

                        if (failedAssignment) continue; // return to top of loop


                        System.out.print("Final operation: " + op.getSimpleName() + "(");
                        for (int paramID = 0; paramID < parameters.length; paramID++) {
                            Field p = parameters[paramID];
                            Class pType = p.getType();
                            Object pValue = p.get(preparedOperation);
                            if (pType == Optional.class) {
                                if (((Optional) pValue).isEmpty())
                                    continue;
                                else {
                                    pType = Util.getTypeFromOptionalParameter(p);
                                    pValue = ((Optional) pValue).get();
                                }
                            }

                            if (paramID != 0) System.out.print(", ");

                            System.out.print(p.getName() + "=");
                            if (pType == String.class) {
                                System.out.print("\"" + pValue + "\"");
                            } else System.out.print(pValue);
                        }
                        System.out.println(")");
                    }

                    System.out.print("Confirm? (y/n) ");
                    String confirmString = scanner.nextLine();
                    if (!confirmString.equalsIgnoreCase("y"))
                        continue; // They don't want this operation. back to top of loop.

                    // Call method with the proper parameters
                    try {
                        gbConn.execute(preparedOperation);
                    } catch (FailedOperationException e) {
                        System.out.println("OPERATION FAILED! " + e.getMessage());
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException _) {}

            } catch (NumberFormatException _) {
                System.out.println("'" + input + "' is not a valid command.");
            }
        }

        gbConn.close();
        System.out.println("Bye!");
    }
}