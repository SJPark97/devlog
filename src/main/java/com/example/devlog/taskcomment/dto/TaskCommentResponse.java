package com.example.devlog.taskcomment.dto;

import com.example.devlog.taskcomment.TaskComment;

import java.time.LocalDateTime;

public record TaskCommentResponse(
        String id,
        String taskId,
        String content,
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
