package com.example.devlog.task.dto;

import com.example.devlog.task.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(
        @NotNull(message = "상태값은 필수입니다..")
        TaskStatus status
) { }
