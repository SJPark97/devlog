package com.example.devlog.common.event;

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

    private void removeProjectEmitter(String projectId, SseEmitter emitter) {
        projectEmitters.computeIfPresent(projectId, (key, list) -> {
            list.remove(emitter);
            if (list.isEmpty()) return null;
            return list;
        });
    }

    public SseEmitter subscribeProject(String projectId) {
        SseEmitter emitter = new SseEmitter(60L * 1000L);
        projectEmitters.computeIfAbsent(projectId, key -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeProjectEmitter(projectId, emitter));
        emitter.onTimeout(() -> removeProjectEmitter(projectId, emitter));
        emitter.onError((exception) -> removeProjectEmitter(projectId, emitter));
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE connected"));
        } catch (IOException exception) {
            emitter.completeWithError(exception);
        }

        return emitter;
    }

    public void publishProject(String projectId, ProjectEventType type, Object data) {
        List<SseEmitter> emitters = projectEmitters.getOrDefault(projectId, List.of());
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(type.name())
                        .data(data));
            } catch (IOException exception) {
                emitter.completeWithError(exception);
                removeProjectEmitter(projectId, emitter);
            }
        }
    }

    @TransactionalEventListener
    public void handleProjectEvent(ProjectEvent event) {
        publishProject(event.projectId(), event.type(), event.data());
    }
}
