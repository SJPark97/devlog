package com.example.devlog.taskcomment;

import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCommentFinder {
    private final TaskCommentRepository taskCommentRepository;

    public TaskComment getTaskCommentById(String id) {
        return taskCommentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_COMMENT_NOT_FOUND));
    }
}
