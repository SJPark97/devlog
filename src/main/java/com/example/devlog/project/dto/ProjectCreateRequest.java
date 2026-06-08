package com.example.devlog.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @Schema(description = "프로젝트 이름", example = "devlog project")
        @NotBlank(message = "프로젝트 이름은 필수입니다.")
        @Size(max = 100, message = "프로젝트 이름은 100자 이하로 입력해주세요.")
        String name,

        @Schema(description = "프로젝트 설명", example = "devlog project description")
        @Size(max = 1000, message = "프로젝트 설명은 1000자 이하로 입력해주세요.")
        String description
) {

}
