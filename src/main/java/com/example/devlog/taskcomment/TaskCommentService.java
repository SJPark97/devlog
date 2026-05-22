package com.example.devlog.taskcomment;

import com.example.devlog.task.Task;
import com.example.devlog.task.TaskFinder;
import com.example.devlog.taskcomment.dto.TaskCommentCreateRequest;
import com.example.devlog.taskcomment.dto.TaskCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskCommentService {

    private final TaskFinder taskFinder;
    private final TaskCommentRepository taskCommentRepository;

    @Transactional
    public TaskCommentResponse createTaskComment(
            @Valid TaskCommentCreateRequest request
    ) {
        Task task = taskFinder.getTaskById(request.taskId());
        TaskComment taskComment = TaskComment.create(task, request.content());
        TaskComment savedTaskComment = taskCommentRepository.save(taskComment);
        return TaskCommentResponse.from(savedTaskComment);
    }
}
