package com.example.devlog.common.event;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProjectEventStore {
    private final Map<String, AtomicLong> ids = new ConcurrentHashMap<>();
    private final Map<String, Deque<StoredEvent>> events = new ConcurrentHashMap<>();
    private static final int MAX_BUFFER_SIZE = 100;

    public StoredEvent save(String projectId, ProjectEventType type, Object data) {
        long id = ids.computeIfAbsent(projectId, key -> new AtomicLong()).incrementAndGet();
        StoredEvent storedEvent = new StoredEvent(id, type, data);
        Deque<StoredEvent> buffer = events.computeIfAbsent(projectId, key -> new ConcurrentLinkedDeque<>());
        buffer.add(storedEvent);
        while (buffer.size() > MAX_BUFFER_SIZE) {
            buffer.pollFirst();
        }
        return storedEvent;
    }

    public List<StoredEvent> findAfter(String projectId, long lastId) {
        Deque<StoredEvent> buffer = events.getOrDefault(projectId, new ConcurrentLinkedDeque<>());
        return buffer.stream()
                .filter(e -> e.id() > lastId)
                .toList();
    }

    @Nullable
    public Long oldestId(String projectId) {
        StoredEvent event = events.getOrDefault(projectId, new ConcurrentLinkedDeque<>()).peekFirst();
        if (event == null) return null;
        return event.id();
    }
}
