package com.inlife.webhook.common;

public class StringUtils {
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
