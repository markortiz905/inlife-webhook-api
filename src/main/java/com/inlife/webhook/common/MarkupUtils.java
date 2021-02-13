package com.inlife.webhook.common;

import java.util.regex.Pattern;

public final class MarkupUtils {

    private static final Pattern MARKUP = Pattern.compile("<[^>]+>");
    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");

    private MarkupUtils() {
    }

    public static String removeMarkup(String text) {
        if (text == null) {
            return "";
        } else {
            text = MARKUP.matcher(text).replaceAll("");
            return WHITE_SPACE.matcher(text).replaceAll(" ").trim();
        }
    }

}
