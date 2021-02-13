package com.inlife.webhook.common;

public class Preconditions {
    public static void checkArgument(boolean var0) {
        if (!var0) {
            throw new IllegalArgumentException();
        }
    }
}
