package com.example.devlog.project.dto;

import com.example.devlog.project.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProjectStatusUpdateRequest(
        @Schema(description = "프로젝트 상태", example = "ACTIVE")
        @NotNull(message = "상태값은 필수입니다.")
        ProjectStatus status
) {

}
