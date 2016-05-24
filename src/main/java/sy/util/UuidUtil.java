package sy.util;

import java.util.UUID;

/**
 * Created by heyh on 16/5/22.
 */
public class UuidUtil {

    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
}
