package com.example.devlog.project.dto;

import com.example.devlog.project.Project;
import com.example.devlog.project.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ProjectResponse(
        @Schema(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        String id,
        @Schema(description = "프로젝트 이름", example = "devlog project")
        String name,
        @Schema(description = "프로젝트 설명", example = "devlog project description")
        String description,
        @Schema(description = "프로젝트 상태", example = "ACTIVE")
        ProjectStatus status,
        @Schema(description = "생성 일시", example = "2026-06-08T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 일시", example = "2026-06-08T10:30:00")
        LocalDateTime updatedAt
) {

    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
