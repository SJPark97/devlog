package com.example.devlog.common.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Event", description = "실시간 알림(SSE) API")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(
            summary = "프로젝트 이벤트 구독",
            description = "프로젝트의 태스크 변경 이벤트(TASK_CREATED, TASK_UPDATED, TASK_DELETED)를 SSE로 구독합니다. 연결은 60초 후 만료됩니다."
    )
    @GetMapping(value = "/projects/{projectId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @Parameter(description = "마지막으로 받은 이벤트 ID")
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId,
            @Parameter(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String projectId
    ) {
        return eventService.subscribeProject(projectId, lastEventId);
    }
}
