package myself.programing.coding.utils;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;

public class HandleExeptionUtils {

    /**
     *
     * @param e
     * @return Throwable
     */
    public static Throwable unwrap(Throwable e) {
        while (e instanceof ExecutionException || e instanceof UndeclaredThrowableException) {
            e = e.getCause();
        }
        return e;
    }
}
