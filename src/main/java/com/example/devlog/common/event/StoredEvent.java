package com.example.devlog.common.event;

public record StoredEvent(
        long id,
        ProjectEventType type,
        Object data
) {
}
