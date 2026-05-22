package com.example.devlog.project.dto;

import com.example.devlog.project.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record ProjectStatusUpdateRequest(
        @NotNull(message = "상태값은 필수입니다.")
        ProjectStatus status
) {

}
