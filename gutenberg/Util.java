package gutenberg;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    /**
     * Retrieves the type from an optional parameter.
     * @param p the Field object representing the optional parameter
     * @return the Class of the type of the parameter
     */
    public static Class getTypeFromOptionalParameter(Field p) {
        return (Class) ((ParameterizedType) p.getGenericType()).getActualTypeArguments()[0];
    }

    /**
     * Attempts to parse a string to create an object of the given type.
     * @param str the String to parse
     * @param type the Class of object to parse the string to
     * @return the parsed object
     *
     * @throws ParseObjectException if the string cannot be parsed to the given type
     * @throws RuntimeException if the given type is not supported for parsing
     */
    public static Object parseObjectFromString(String str, Class type) {
        if (type == String.class) {
            return str;
        }

        try {
            if (type == int.class || type == Integer.class) {
                return Integer.parseInt(str);
            }

            if (type == float.class || type == Float.class) {
                return Integer.parseInt(str);
            }

            if (type == LocalDate.class) {
                return LocalDate.parse(str);
            }

            throw new RuntimeException(type.getSimpleName() + " is not a parseable type.");
        } catch (NumberFormatException | DateTimeParseException _) {
            throw new ParseObjectException("Could not parse '" + str + "' as " + type.getSimpleName(), type);
        }
    }

    /**
     * An exception that represents a failure to parse a string to an arbitrary type.
     */
    public static class ParseObjectException extends RuntimeException {
        Class parseType;

        public ParseObjectException(String message, Class parseType) {
            super(message);
            this.parseType = parseType;
        }
    }

    public static String sqlStrWrapper(String str) {
        return (str.isEmpty()) ? "NULL" : "\"" + str + "\"";
    }

    public static String sqlStrWrapper(Optional<String> str) {
        return (str.isEmpty()) ? "NULL" :sqlStrWrapper(str.get());
    }

    public static void printTable(String tableName, List<String> headers, List<List<Object>> data) {
        // constants used for spacing
        int MIN_COLUMN_WIDTH = 5;
        int COLUMN_PADDING = 2;

        // Print table title
        System.out.print("--- ");
        System.out.print(tableName);
        System.out.println(" ---");
        if (data.isEmpty()) {
            System.out.println("EMPTY TABLE");
            return;
        }

        // Initialize the column widths (minimum column width is 5)
        List<Integer> columnWidths = new ArrayList<>(Collections.nCopies(headers.size(), MIN_COLUMN_WIDTH));
        List<List<String>> stringifiedData = new ArrayList<>();

        // Loop through headers and keep track of column widths
        for (int i = 0; i < headers.size(); i++) {
            int columnWidth = columnWidths.get(i);
            String header = headers.get(i);
            int paddedWidth = header.length() + COLUMN_PADDING;
            if (paddedWidth > columnWidth) columnWidths.set(i, paddedWidth);
        }

        // Loop through the data. Stringify data and keep track of greatest width per column
        for (List<Object> row : data) {
            List<String> stringifiedRow = new ArrayList<>(row.size());
            int i = 0;
            for (Object d : row) {
                String s = (d == null) ? "N/A" : d.toString();
                stringifiedRow.add(s);
                int currentMaxLength = columnWidths.get(i);
                int paddedWidth = s.length() + COLUMN_PADDING;
                if (paddedWidth > currentMaxLength)
                    columnWidths.set(i, paddedWidth);
                i++;
            }

            stringifiedData.add(stringifiedRow);
        }

        // Print headers
        for (int i = 0; i < headers.size(); i++) {
            int columnWidth = columnWidths.get(i);
            if (i != 0) System.out.print(" | ");
            System.out.printf("%-" + columnWidth + "s", headers.get(i));
        }
        System.out.println();

        // Fancy separator
        int totalTableWidth = columnWidths.stream().mapToInt(Integer::intValue).sum() + (columnWidths.size() - 1) * 3;
        System.out.println("-".repeat(totalTableWidth));

        // Print data rows
        for (List<String> row : stringifiedData) {
            for (int i = 0; i < row.size(); i++) {
                int columnWidth = columnWidths.get(i);
                if (i != 0) System.out.print(" | ");
                System.out.printf("%-" + columnWidth + "s", row.get(i));
            }

            System.out.println();
        }

        // Fancy closing separator
        System.out.println("-".repeat(totalTableWidth));
    }
}
