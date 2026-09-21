package com.example.devlog.common.event;

import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProjectEventStore {
    private final Map<String, AtomicLong> ids = new ConcurrentHashMap<>();
    private final Map<String, Deque<StoredEvent>> events = new ConcurrentHashMap<>();
    private static final int MAX_BUFFER_SIZE = 100;

    StoredEvent save(String projectId, ProjectEventType type, Object data) {
        long id = ids.computeIfAbsent(projectId, key -> new AtomicLong()).incrementAndGet();
        StoredEvent storedEvent = new StoredEvent(id, type, data);
        Deque<StoredEvent> event = events.computeIfAbsent(projectId, key -> new ConcurrentLinkedDeque<>());
        event.add(storedEvent);
        while (event.size() > MAX_BUFFER_SIZE) {
            event.pollFirst();
        }
        return storedEvent;
    }
}
