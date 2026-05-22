package com.example.devlog.taskcomment;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.taskcomment.dto.TaskCommentCreateRequest;
import com.example.devlog.taskcomment.dto.TaskCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-comments")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskCommentResponse> createTaskComment(
            @Valid @RequestBody TaskCommentCreateRequest request
    ) {
        TaskCommentResponse response = taskCommentService.createTaskComment(request);
        return ApiResponse.created(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<TaskCommentResponse>> getTaskComments(
        @RequestParam String taskId
    ) {
        List<TaskCommentResponse> responses = taskCommentService.getTaskComments(taskId);
        return ApiResponse.success(responses);
    }
}
