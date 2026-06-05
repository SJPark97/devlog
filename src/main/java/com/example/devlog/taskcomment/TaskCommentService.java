package com.example.devlog.taskcomment;

import com.example.devlog.task.Task;
import com.example.devlog.task.TaskFinder;
import com.example.devlog.taskcomment.dto.TaskCommentCreateRequest;
import com.example.devlog.taskcomment.dto.TaskCommentResponse;
import com.example.devlog.taskcomment.dto.TaskCommentUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCommentService {

    private final TaskFinder taskFinder;
    private final TaskCommentFinder taskCommentFinder;
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

    @Transactional(readOnly = true)
    public List<TaskCommentResponse> getTaskComments(String taskId) {
        Task task = taskFinder.getTaskById(taskId);
        return taskCommentRepository.findByTaskIdAndDeletedFalseOrderByCreatedAtDesc(task.getId())
                .stream()
                .map(TaskCommentResponse::from)
                .toList();
    }

    @Transactional
    public TaskCommentResponse updateTaskComment(
            String id,
            @Valid TaskCommentUpdateRequest request
    ) {
        TaskComment taskComment = taskCommentFinder.getTaskCommentById(id);
        taskComment.update(request.content());
        return TaskCommentResponse.from(taskComment);
    }

    @Transactional
    public void deleteTaskComment(
            String id
    ) {
        TaskComment taskComment = taskCommentFinder.getTaskCommentById(id);
        taskComment.delete();
    }
}
