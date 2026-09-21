package com.example.devlog.common.event;

import com.example.devlog.common.util.NumberUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class EventService {

    private final Map<String, List<SseEmitter>> projectEmitters = new ConcurrentHashMap<>();
    private final ProjectEventStore projectEventStore;

    protected SseEmitter createSseEmitter() {
        return new SseEmitter(60L * 1000L);
    }

    private void removeProjectEmitter(String projectId, SseEmitter emitter) {
        projectEmitters.computeIfPresent(projectId, (key, list) -> {
            list.remove(emitter);
            if (list.isEmpty()) return null;
            return list;
        });
    }

    private void send(String projectId, SseEmitter emitter, SseEmitter.SseEventBuilder builder) {
        try {
            emitter.send(builder);
        } catch (IOException exception) {
            emitter.completeWithError(exception);
            removeProjectEmitter(projectId, emitter);
        }
    }

    public SseEmitter subscribeProject(String projectId, @Nullable String lastEventId) {
        SseEmitter emitter = createSseEmitter();
        projectEmitters.computeIfAbsent(projectId, key -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeProjectEmitter(projectId, emitter));
        emitter.onTimeout(() -> removeProjectEmitter(projectId, emitter));
        emitter.onError((exception) -> removeProjectEmitter(projectId, emitter));
        SseEmitter.SseEventBuilder builder = SseEmitter.event()
                                                        .name("connect")
                                                        .data("SSE connected");
        send(projectId, emitter, builder);
        Long lastId = NumberUtils.parseLongOrNull(lastEventId);
        if (lastId != null) {
            List<StoredEvent> storedEvents = projectEventStore.findAfter(projectId, lastId);
            for (StoredEvent storeEvent:storedEvents) {
                send(projectId, emitter, SseEmitter.event()
                        .id(String.valueOf(storeEvent.id()))
                        .name(storeEvent.type().name())
                        .data(storeEvent.data()));
            }
        }

        return emitter;
    }

    public void publishProject(String projectId, ProjectEventType type, Object data) {
        List<SseEmitter> emitters = projectEmitters.getOrDefault(projectId, List.of());
        StoredEvent stored = projectEventStore.save(projectId, type, data);
        for (SseEmitter emitter : emitters) {
            send(projectId, emitter, SseEmitter.event()
                .id(String.valueOf(stored.id()))
                .name(type.name())
                .data(data));
        }
    }

    @TransactionalEventListener
    public void handleProjectEvent(ProjectEvent event) {
        publishProject(event.projectId(), event.type(), event.data());
    }
}
