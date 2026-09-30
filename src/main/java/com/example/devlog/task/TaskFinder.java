package com.example.devlog.task;

import com.example.devlog.common.deletion.DeletionValidator;
import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskFinder {
    private final TaskRepository taskRepository;

    public Task getTaskById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
        DeletionValidator.validateNotDeleted(task.deletionCheck());
        return task;
    }
}
