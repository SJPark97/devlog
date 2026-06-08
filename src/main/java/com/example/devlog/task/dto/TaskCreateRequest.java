package com.example.devlog.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(

        @Schema(description = "프로젝트 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
        @NotBlank(message = "프로젝트 ID는 필수입니다.")
        String projectId,

        @Schema(description = "작업 제목", example = "프로젝트 API 구현")
        @NotBlank(message = "태스크 이름은 필수입니다.")
        @Size(max = 100, message = "태스크 이름은 100자 이하로 입력해주세요.")
        String title,

        @Schema(description = "작업 내용", example = "프로젝트 생성, 조회 API를 구현합니다.")
        @Size(max = 1000, message = "태스크 내용은 1000자 이하로 입력해주세요.")
        String content
) {

}
