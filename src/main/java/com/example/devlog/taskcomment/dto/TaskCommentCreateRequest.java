package com.example.devlog.taskcomment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCommentCreateRequest(
        @Schema(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
        @NotBlank(message = "태스크 ID는 필수입니다.")
        String taskId,

        @Schema(description = "댓글 내용", example = "첫 댓글입니다.")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 1000, message = "댓글은 1000자 이하로 작성해주세요.")
        String content
) {

}
