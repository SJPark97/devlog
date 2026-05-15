package com.example.devlog.project;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ProjectStatus {
    ACTIVE,
    DONE,
    HOLD,
    ARCHIVED;

    public static String getAvailableValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
