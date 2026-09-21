package com.example.devlog.common.event;

import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class EventServiceTest {

    /**
     * 실제 SSE 연결 대신 전송 횟수만 세는 테스트용 emitter.
     * SseEmitter 에는 "무엇을 보냈는지" 꺼내 볼 수 있는 메서드가 없어서,
     * send() 를 가로채 호출 횟수를 기록하는 방식으로 검증한다.
     */
    static class RecordingEmitter extends SseEmitter {
        int sendCount = 0;
        @Override
        public void send(@NonNull SseEventBuilder builder) {
            sendCount++;
        }
    }

    /**
     * 진짜 SseEmitter 대신 RecordingEmitter 를 만들어주는 EventService.
     * EventService 가 emitter 를 내부에서 직접 생성하면 테스트가 가로챌 수 없으므로,
     * 생성 부분만 createSseEmitter() 로 분리해두고 여기서 바꿔치기한다.
     * Map 관리·발행 로직은 부모 것을 그대로 쓰므로 실제 동작과 같다.
     */
    static class TestEventService extends EventService {
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
        SseEmitter emitter = eventService.subscribeProject("project-1");
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
        RecordingEmitter emitter1 = (RecordingEmitter) eventService.subscribeProject(projectId);
        RecordingEmitter emitter2 = (RecordingEmitter) eventService.subscribeProject(projectId);
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
        RecordingEmitter emitter1 = (RecordingEmitter) eventService.subscribeProject("project-1");
        RecordingEmitter emitter2 = (RecordingEmitter) eventService.subscribeProject("project-2");
        eventService.publishProject("project-1", ProjectEventType.TASK_CREATED, "테스트 데이터");
        Assertions.assertThat(emitter1.sendCount).isEqualTo(2);
        Assertions.assertThat(emitter2.sendCount).isEqualTo(1);
    }

    /*
     * 다음 단계: 연결이 끊긴 emitter 는 구독 목록에서 제거되는지 확인한다.
     *
     * publishProject_removesEmitterWhenSendFails
     *
     * given  - send 호출 시 IOException 을 던지는 emitter 로 구독
     * when   - 같은 프로젝트에 두 번 발행
     * then   - 첫 발행에서 제거되므로 두 번째 발행은 그 emitter 에 도달하지 않는다
     *          (실패한 emitter 의 send 호출 횟수가 더 늘지 않아야 한다)
     *
     * 준비물: RecordingEmitter 처럼 send() 를 재정의하되 IOException 을 던지는 emitter 가 필요하다.
     *        EventService.publishProject 의 catch 블록(removeProjectEmitter)이 검증 대상.
     *
     * 이후 후보
     * - 구독자가 없는 프로젝트에 발행해도 예외가 나지 않는다
     * - 구독이 모두 해제되면 빈 프로젝트 key 도 Map 에서 사라진다 (computeIfPresent 의 null 반환)
     */
}
