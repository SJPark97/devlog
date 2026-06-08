package com.example.devlog.taskcomment;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.taskcomment.dto.TaskCommentCreateRequest;
import com.example.devlog.taskcomment.dto.TaskCommentResponse;
import com.example.devlog.taskcomment.dto.TaskCommentUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task Comment", description = "작업 댓글 API")
@RestController
@RequestMapping("/api/task-comments")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @Operation(summary = "작업 댓글 생성", description = "작업에 댓글을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskCommentResponse> createTaskComment(
            @Valid @RequestBody TaskCommentCreateRequest request
    ) {
        TaskCommentResponse response = taskCommentService.createTaskComment(request);
        return ApiResponse.created(response);
    }

    @Operation(summary = "작업 댓글 목록 조회", description = "작업 ID에 해당하는 댓글 목록을 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<TaskCommentResponse>> getTaskComments(
            @Parameter(description = "작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
            @RequestParam String taskId
    ) {
        List<TaskCommentResponse> responses = taskCommentService.getTaskComments(taskId);
        return ApiResponse.success(responses);
    }

    @Operation(summary = "작업 댓글 수정", description = "댓글 내용을 수정합니다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TaskCommentResponse> updateTaskComment(
            @Parameter(description = "댓글 ID", example = "3eaec3bf-40c0-480d-b879-1cb9b4608c65")
            @PathVariable String id,
            @Valid @RequestBody TaskCommentUpdateRequest request
    ) {
        TaskCommentResponse response = taskCommentService.updateTaskComment(id, request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "작업 댓글 삭제", description = "댓글을 삭제 처리합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteTaskComment(
            @Parameter(description = "댓글 ID", example = "3eaec3bf-40c0-480d-b879-1cb9b4608c65")
            @PathVariable String id
    ) {
        taskCommentService.deleteTaskComment(id);
        return ApiResponse.success(null, "댓글이 삭제되었습니다.");
    }
}
