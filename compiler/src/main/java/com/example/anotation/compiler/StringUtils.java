package com.example.anotation.compiler;

public class StringUtils {

    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }
}
