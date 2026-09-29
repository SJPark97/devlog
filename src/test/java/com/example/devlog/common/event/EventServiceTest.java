package com.example.devlog.common.event;

import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class EventServiceTest {

    /**
     * 실제 SSE 연결 대신 전송 횟수와 이벤트 이름을 기록하는 테스트용 emitter.
     * SseEmitter 에는 보낸 내용을 꺼내는 메서드가 없어서 send() 를 가로채 기록한다.
     * 이름은 build() 조각 안의 "event:" 줄에서 뗀다. (조각 위치는 Spring 내부 사정이라 전부 돈다)
     */
    static class RecordingEmitter extends SseEmitter {
        int sendCount = 0;
        boolean failOnSend = false;
        List<String> eventNames = new ArrayList<>();
        @Override
        public void send(@NonNull SseEventBuilder builder) throws IOException {
            sendCount++;
            for (DataWithMediaType piece : builder.build()) {
                if (piece.getData() instanceof String text) {
                    text.lines()
                            .filter(line -> line.startsWith("event:"))
                            .map(line -> line.substring("event:".length()))
                            .forEach(eventNames::add);
                }
            }
            if (failOnSend) throw new IOException("연결 끊김");
        }
    }

    /**
     * 진짜 SseEmitter 대신 RecordingEmitter 를 만들어주는 EventService.
     * EventService 가 emitter 를 내부에서 직접 생성하면 테스트가 가로챌 수 없으므로,
     * 생성 부분만 createSseEmitter() 로 분리해두고 여기서 바꿔치기한다.
     * Map 관리·발행 로직은 부모 것을 그대로 쓰므로 실제 동작과 같다.
     *
     * ProjectEventStore 는 가짜를 쓰지 않고 진짜를 넘긴다.
     * 메모리 Map 뿐이라 DB 가 필요 없고, 테스트마다 새로 만들어져 서로 영향이 없다.
     * 단 여기서 만들어 버리므로 테스트가 store 를 직접 들여다볼 수는 없다.
     * 버퍼를 미리 채워야 하는 재생 테스트는 store 대신 publishProject 로 채운다.
     */
    static class TestEventService extends EventService {
        public TestEventService() {
            super(new ProjectEventStore());
        }

        @Override
        protected SseEmitter createSseEmitter() {
            return new RecordingEmitter();
        }
    }

    /**
     * 구독하면 emitter 를 돌려주는지 확인한다.
     * Spring 컨텍스트 없이 new 로 만들어 검증하므로 1초 안에 끝난다.
     * (@SpringBootTest 를 붙이면 서버 전체가 떠서 불필요하게 느려진다)
     */
    @Test
    void subscribeProject_returnsEmitter() {
        TestEventService eventService = new TestEventService();
        SseEmitter emitter = eventService.subscribeProject("project-1", null);
        Assertions.assertThat(emitter).isNotNull();
    }

    /**
     * 같은 프로젝트를 구독한 사람이 여러 명이면 전원에게 이벤트가 가는지 확인한다.
     * curl 로는 확인하기 번거로웠던 부분.
     *
     * sendCount 가 2인 이유: 구독 시 connect 1번 + TASK_CREATED 1번.
     */
    @Test
    void publishProject_sendsEventToAllSubscribersOfProject() {
        TestEventService eventService = new TestEventService();
        String projectId = "project-1";
        RecordingEmitter emitter1 = (RecordingEmitter) eventService.subscribeProject(projectId, null);
        RecordingEmitter emitter2 = (RecordingEmitter) eventService.subscribeProject(projectId, null);
        eventService.publishProject(projectId, ProjectEventType.TASK_CREATED, "테스트 데이터");
        Assertions.assertThat(emitter1.sendCount).isEqualTo(2);
        Assertions.assertThat(emitter2.sendCount).isEqualTo(2);
    }

    /**
     * 프로젝트별 격리 검증. 이 기능의 핵심이라 반드시 지켜져야 한다.
     *
     * project-2 구독자의 sendCount 가 0이 아니라 1인 이유: 구독 시 connect 는 받기 때문.
     * 여기서 2가 나오면 다른 프로젝트 이벤트까지 받고 있다는 뜻이다.
     */
    @Test
    void publishProject_doesNotSendEventToOtherProjectSubscribers() {
        TestEventService eventService = new TestEventService();
        RecordingEmitter emitter1 = (RecordingEmitter) eventService.subscribeProject("project-1", null);
        RecordingEmitter emitter2 = (RecordingEmitter) eventService.subscribeProject("project-2", null);
        eventService.publishProject("project-1", ProjectEventType.TASK_CREATED, "테스트 데이터");
        Assertions.assertThat(emitter1.sendCount).isEqualTo(2);
        Assertions.assertThat(emitter2.sendCount).isEqualTo(1);
    }

    /**
     * 연결이 끊긴 emitter 가 구독 목록에서 제거되는지 확인한다.
     * publishProject 의 catch 블록이 검증 대상 — 평소에는 실행되지 않는 경로라 버그가 숨기 쉽다.
     *
     * 흐름: 구독 1(connect 성공) → failOnSend on → 1차 발행 2(호출됨, 예외 → 제거)
     *       → 2차 발행 2(제거돼서 호출조차 안 됨)
     *
     * 마지막 2가 핵심. 제거가 안 되면 3이 되어 실패한다.
     * RecordingEmitter 가 던지기 전에 sendCount 를 먼저 올리는 이유도 이것.
     * 순서가 반대면 "제거돼서 2"인지 "세기 전에 예외라서 2"인지 구분할 수 없어 테스트가 무의미해진다.
     */
    @Test
    void publishProject_removesEmitterWhenSendFails() {
        TestEventService eventService = new TestEventService();
        RecordingEmitter emitter = (RecordingEmitter) eventService.subscribeProject("project-1", null);
        emitter.failOnSend = true;
        eventService.publishProject("project-1", ProjectEventType.TASK_CREATED, "테스트 데이터");
        eventService.publishProject("project-1", ProjectEventType.TASK_CREATED, "테스트 데이터");
        Assertions.assertThat(emitter.sendCount).isEqualTo(2);
    }

    /**
     * 재연결 시 Last-Event-ID(5) 이후 이벤트만 재생하는지 확인한다.
     * 46 = connect 1 + 6~50번 45개. (2 면 resync 로 빠짐, 51 이면 처음부터 재생)
     * 버퍼는 publishProject 로 채워 발행 경로까지 같이 검증한다.
     */
    @Test
    void subscribeProject_replaysEventsAfterLastEventId() {
        TestEventService eventService = new TestEventService();
        int n = 50;
        for (int i = 1; i <= n; i++) {
            eventService.publishProject("project", ProjectEventType.TASK_CREATED, Map.of("data", "테스트 태스크", "cnt", i));
        };
        RecordingEmitter emitter = (RecordingEmitter) eventService.subscribeProject("project", "5");
        Assertions.assertThat(emitter.sendCount).isEqualTo(46);
    }

    /**
     * 놓친 이벤트(6~100번)가 버퍼에서 밀려났으면 재생 대신 resync 를 보내는지 확인한다.
     * containsExactly 라서 resync 뒤에 재생이 이어지는 실수(return 누락)도 잡는다.
     */
    @Test
    void subscribeProject_sendsResyncWhenMissedEventsFellOutOfBuffer() {
        TestEventService eventService = new TestEventService();
        int n = 200;
        for (int i = 1; i <= n; i++) {
            eventService.publishProject("project", ProjectEventType.TASK_CREATED, Map.of("data", "테스트 태스크", "cnt", i));
        };
        RecordingEmitter emitter = (RecordingEmitter) eventService.subscribeProject("project", "5");
        Assertions.assertThat(emitter.eventNames).containsExactly("connect", "resync");
    }
}
