package com.example.devlog.task.dto;

import com.example.devlog.task.Task;
import com.example.devlog.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TaskResponse(
        @Schema(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
        String id,
        @Schema(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        String projectId,
        @Schema(description = "작업 제목", example = "프로젝트 API 구현")
        String title,
        @Schema(description = "작업 내용", example = "프로젝트 생성, 조회 API를 구현합니다.")
        String content,
        @Schema(description = "작업 상태", example = "TODO")
        TaskStatus status,
        @Schema(description = "작업 시작 일시", example = "2026-06-08T10:00:00")
        LocalDateTime startedAt,
        @Schema(description = "작업 완료 일시", example = "2026-06-08T11:00:00")
        LocalDateTime completedAt,
        @Schema(description = "생성 일시", example = "2026-06-08T09:30:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 일시", example = "2026-06-08T10:30:00")
        LocalDateTime updatedAt
) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getProject().getId(),
                task.getTitle(),
                task.getContent(),
                task.getStatus(),
                task.getStartedAt(),
                task.getCompletedAt(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
