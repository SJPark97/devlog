package com.example.devlog.task;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.task.dto.TaskCreateRequest;
import com.example.devlog.task.dto.TaskResponse;
import com.example.devlog.task.dto.TaskStatusUpdateRequest;
import com.example.devlog.task.dto.TaskUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<TaskResponse>> getTasks(
            @RequestParam(required = true) String projectId
    ) {
        List<TaskResponse> response = taskService.getTasks(projectId);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskResponse> getTask(
            @PathVariable String id
    ) {
        TaskResponse response = taskService.getTask(id);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskResponse> updateTask(
            @PathVariable String id,
            @Valid @RequestBody TaskUpdateRequest requestBody
    ) {
        TaskResponse response = taskService.updateTask(id, requestBody);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskResponse> updateTaskStatus(
            @PathVariable String id,
            @Valid @RequestBody TaskStatusUpdateRequest requestBody
    ) {
        TaskResponse response = taskService.updateTaskStatus(id, requestBody);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ApiResponse.success(null, "태스크가 삭제되었습니다.");
    }
}
