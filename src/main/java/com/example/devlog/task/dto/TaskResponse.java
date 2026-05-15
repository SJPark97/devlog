package com.example.devlog.task.dto;

import com.example.devlog.project.Project;
import com.example.devlog.task.Task;
import com.example.devlog.task.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        String id,
        String projectId,
        String title,
        String content,
        TaskStatus status,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime createdAt,
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
