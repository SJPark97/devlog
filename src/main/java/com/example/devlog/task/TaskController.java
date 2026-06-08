package com.example.devlog.task;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.task.dto.TaskCreateRequest;
import com.example.devlog.task.dto.TaskResponse;
import com.example.devlog.task.dto.TaskStatusUpdateRequest;
import com.example.devlog.task.dto.TaskUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task", description = "작업 API")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "작업 생성", description = "프로젝트에 새 작업을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskResponse response = taskService.createTask(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "작업 목록 조회", description = "프로젝트 ID에 해당하는 작업 목록을 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<TaskResponse>> getTasks(
            @Parameter(description = "프로젝트 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
            @RequestParam(required = true) String projectId
    ) {
        List<TaskResponse> response = taskService.getTasks(projectId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "작업 상세 조회", description = "작업 ID로 작업 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskResponse> getTask(
            @Parameter(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
            @PathVariable String id
    ) {
        TaskResponse response = taskService.getTask(id);
        return ApiResponse.success(response);
    }

    @Operation(summary = "작업 수정", description = "작업 제목과 내용을 수정합니다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskResponse> updateTask(
            @Parameter(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
            @PathVariable String id,
            @Valid @RequestBody TaskUpdateRequest requestBody
    ) {
        TaskResponse response = taskService.updateTask(id, requestBody);
        return ApiResponse.success(response);
    }

    @Operation(summary = "작업 상태 변경", description = "작업 상태를 변경합니다.")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskResponse> updateTaskStatus(
            @Parameter(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
            @PathVariable String id,
            @Valid @RequestBody TaskStatusUpdateRequest requestBody
    ) {
        TaskResponse response = taskService.updateTaskStatus(id, requestBody);
        return ApiResponse.success(response);
    }

    @Operation(summary = "작업 삭제", description = "작업을 삭제 처리합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteTask(
            @Parameter(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
            @PathVariable String id
    ) {
        taskService.deleteTask(id);
        return ApiResponse.success(null, "태스크가 삭제되었습니다.");
    }
}
