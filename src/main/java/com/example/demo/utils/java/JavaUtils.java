package com.example.demo.utils.java;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

@UtilityClass
public class JavaUtils {

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static <T, R> R getOrNull(T object, Function<T, R> function) {
        return getOrDefault(object, function, null);
    }

    public static <T, U> U getOrDefault(T object, Function<T, U> function, U defaultValue) {
        return ofNullable(object)
                .map(function)
                .orElse(defaultValue);
    }

    public static String getFileExtension(String name) {
        try {
            return name.substring(name.lastIndexOf('.') + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFileNameWithoutExtension(String name) {
        try {
            return name.substring(0, name.lastIndexOf('.'));
        } catch (Exception e) {
            return "";
        }
    }

}
