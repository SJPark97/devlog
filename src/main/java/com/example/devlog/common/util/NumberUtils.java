package com.example.devlog.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@Slf4j
public final class NumberUtils {
    private NumberUtils() {
    }

    @Nullable
    public static Long parseLongOrNull(@Nullable String value) {
        try {
            if (value == null) return null;
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
