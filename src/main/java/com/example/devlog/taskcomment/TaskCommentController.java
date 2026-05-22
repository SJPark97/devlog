package com.example.devlog.taskcomment;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.taskcomment.dto.TaskCommentCreateRequest;
import com.example.devlog.taskcomment.dto.TaskCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
