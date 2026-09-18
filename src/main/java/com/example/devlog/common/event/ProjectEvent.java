package com.example.devlog.common.event;

public record ProjectEvent(
        String projectId,
        String eventName,
        Object data
) {
}
