package mx.sekura.cancelationMassive.Entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class DictionaryCancellation {
    public static Map<Integer,String> mapperReasonCancellation;
    static {
        Map<Integer, String> mapperReason = new HashMap<>();
        mapperReason.put(8,"Cumplimiento de la obligación");
        mapperReasonCancellation = Collections.unmodifiableMap(mapperReason);
    }
}
