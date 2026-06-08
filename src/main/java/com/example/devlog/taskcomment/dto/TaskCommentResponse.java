package com.example.devlog.taskcomment.dto;

import com.example.devlog.taskcomment.TaskComment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TaskCommentResponse(
        @Schema(description = "댓글 ID", example = "3eaec3bf-40c0-480d-b879-1cb9b4608c65")
        String id,
        @Schema(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
        String taskId,
        @Schema(description = "댓글 내용", example = "첫 댓글입니다.")
        String content,
        @Schema(description = "생성 일시", example = "2026-06-08T09:39:51")
        LocalDateTime createdAt
) {
    public static TaskCommentResponse from(TaskComment taskComment) {
        return new TaskCommentResponse(
                taskComment.getId(),
                taskComment.getTask().getId(),
                taskComment.getContent(),
                taskComment.getCreatedAt()
        );
    }
}
