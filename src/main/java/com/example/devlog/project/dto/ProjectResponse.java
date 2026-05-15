package com.example.devlog.project.dto;

import com.example.devlog.project.Project;

import java.time.LocalDateTime;

public record ProjectResponse(String id, String name, String description, String status, LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

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