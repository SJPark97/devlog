package com.example.devlog.project;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.common.dto.PageResponse;
import com.example.devlog.project.dto.ProjectCreateRequest;
import com.example.devlog.project.dto.ProjectResponse;
import com.example.devlog.project.dto.ProjectStatusUpdateRequest;
import com.example.devlog.project.dto.ProjectUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project", description = "프로젝트 API")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return ApiResponse.created(response);
    }

    @Operation(summary = "프로젝트 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getProjects(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "정렬 기준 필드", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방향", example = "desc")
            @RequestParam(defaultValue = "desc") String direction,
            @Parameter(description = "프로젝트 이름 검색어", example = "devlog")
            @RequestParam(required = false) String keyword
    ) {
        if (page != null && size != null) {
            Page<ProjectResponse> response = projectService.getProjectsPage(page, size, sortBy, direction, keyword);
            return ApiResponse.success(PageResponse.from(response));
        }
        List<ProjectResponse> response = projectService.getProjects();
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로젝트 상세 조회")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectResponse> getProject(
            @Parameter(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id
    ) {
        ProjectResponse response = projectService.getProject(id);
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로젝트 수정")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectResponse> updateProject(
            @Parameter(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id,
            @Valid @RequestBody ProjectUpdateRequest request
    ) {
        ProjectResponse response = projectService.updateProject(id, request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로젝트 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteProject(
            @Parameter(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id
    ) {
        projectService.deleteProject(id);
        return ApiResponse.success(null, "프로젝트가 삭제되었습니다.");
    }

    @Operation(summary = "프로젝트 상태 변경")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectResponse> updateProjectStatus(
            @Parameter(description = "프로젝트 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id,
            @Valid @RequestBody ProjectStatusUpdateRequest request
    ) {
        ProjectResponse response = projectService.updateProjectStatus(id, request);
        return ApiResponse.success(response);
    }
}
