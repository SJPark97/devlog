package com.example.devlog.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @Schema(description = "작업 제목", example = "프로젝트 API 수정")
        @NotBlank(message = "작업 제목은 필수입니다.")
        @Size(max = 200, message = "작업 제목은 200자 이하로 입력해주세요.")
        String title,

        @Schema(description = "작업 내용", example = "프로젝트 API 응답 DTO를 수정합니다.")
        @NotBlank(message = "작업 내용은 필수입니다.")
        @Size(max = 1000, message = "작업 내용은 1000자 이하로 입력해주세요.")
        String content
) {
}
