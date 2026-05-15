package com.example.devlog.task;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.task.dto.TaskCreateRequest;
import com.example.devlog.task.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskResponse response = taskService.createTask(request);
        return ApiResponse.success(response);
    }

}
