package com.example.devlog.task;

import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskFinder {
    private final TaskRepository taskRepository;

    public Task getTaskById(String id) {
        return taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
    }
}
