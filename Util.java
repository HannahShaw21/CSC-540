import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class Util {

    public static Class getTypeFromOptionalParameter(Field p) {
        return (Class) ((ParameterizedType) p.getGenericType()).getActualTypeArguments()[0];
    }
}
