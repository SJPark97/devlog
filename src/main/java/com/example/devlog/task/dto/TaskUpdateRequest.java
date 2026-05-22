package com.example.devlog.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @NotBlank(message = "작업 제목은 필수입니다.")
        @Size(max = 200, message = "작업 제목은 200자 이하로 입력해주세요.")
        String title,

        @NotBlank(message = "작업 내용은 필수입니다.")
        @Size(max = 1000, message = "작업 내용은 1000자 이하로 입력해주세요.")
        String content
) {
}