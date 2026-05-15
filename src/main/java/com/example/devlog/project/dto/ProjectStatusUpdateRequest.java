package com.example.devlog.project.dto;

import com.example.devlog.project.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectStatusUpdateRequest {

    @NotNull(message = "상태값은 필수입니다.")
    private ProjectStatus status;
}
