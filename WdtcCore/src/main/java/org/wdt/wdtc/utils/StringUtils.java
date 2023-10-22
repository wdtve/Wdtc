package org.wdt.wdtc.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringUtils {
    public static final String STRING_EMPTY = "";
    public static final String STRING_SPACE = " ";

    public static String cleanStrInString(String string, String... strs) {
        for (String str : strs) {
            string = string.replace(str, STRING_EMPTY);
        }
        return string;
    }

    public static String appendForString(Object o, Object... strings) {
        StringBuilder string = new StringBuilder(o.toString());
        for (Object str : strings) {
            string.append(str);
        }
        return string.toString();
    }

    public static String StringToBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
}
