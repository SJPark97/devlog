package com.example.devlog.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(

        @NotBlank(message = "프로젝트 ID는 필수입니다.")
        String projectId,

        @NotBlank(message = "태스크 이름은 필수입니다.")
        @Size(max = 100, message = "태스크 이름은 100자 이하로 입력해주세요.")
        String title,

        @Size(max = 1000, message = "태스크 내용은 1000자 이하로 입력해주세요.")
        String content
) {

}
