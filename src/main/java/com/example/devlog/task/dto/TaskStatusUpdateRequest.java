package com.example.devlog.task.dto;

import com.example.devlog.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(
        @Schema(description = "작업 상태", example = "IN_PROGRESS")
        @NotNull(message = "상태값은 필수입니다..")
        TaskStatus status
) { }
