package gutenberg;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

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
            if (type == int.class) {
                return Integer.parseInt(str);
            }

            if (type == float.class) {
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
        return "\"" + str + "\"";
    }

    public static String sqlStrWrapper(Optional<String> str) {
        if (str.isEmpty()) return "NULL";
        return sqlStrWrapper(str.get());
    }
}
