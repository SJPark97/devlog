package com.example.devlog.common.event;

import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class EventServiceTest {
    static class RecordingEmitter extends SseEmitter {
        int sendCount = 0;
        @Override
        public void send(@NonNull SseEventBuilder builder) {
            sendCount++;
        }
    }
    static class TestEventService extends EventService {
        @Override
        protected SseEmitter createSseEmitter() {
            return new RecordingEmitter();
        }
    }

    @Test
    void subscribeProject_returnsEmitter() {
        TestEventService eventService = new TestEventService();
        SseEmitter emitter = eventService.subscribeProject("project-1");
        Assertions.assertThat(emitter).isNotNull();
    }

    @Test
    void publishProject_sendsEventToAllSubscribersOfProject() {
        TestEventService eventService = new TestEventService();
        String projectId = "project-1";
        RecordingEmitter emitter1 = (RecordingEmitter) eventService.subscribeProject(projectId);
        RecordingEmitter emitter2 = (RecordingEmitter) eventService.subscribeProject(projectId);
        eventService.publishProject(projectId, ProjectEventType.TASK_CREATED, "테스트 데이터");
        Assertions.assertThat(emitter1.sendCount).isEqualTo(2);
        Assertions.assertThat(emitter2.sendCount).isEqualTo(2);
    }
}
