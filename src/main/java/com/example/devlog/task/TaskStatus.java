package com.example.devlog.task;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    HOLD,
    CANCELLED;

    public static String getAvailableValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
