package com.example.devlog.common.event;

public record ProjectEvent(
        String projectId,
        ProjectEventType type,
        Object data
) {
}
