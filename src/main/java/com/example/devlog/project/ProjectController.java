package com.example.devlog.project;

import com.example.devlog.common.ApiResponse;
import com.example.devlog.project.dto.ProjectCreateRequest;
import com.example.devlog.project.dto.ProjectResponse;
import com.example.devlog.project.dto.ProjectStatusUpdateRequest;
import com.example.devlog.project.dto.ProjectUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectResponse> createProject(@RequestBody ProjectCreateRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return ApiResponse.success(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ProjectResponse>> getProjects() {
        List<ProjectResponse> response = projectService.getProjects();
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectResponse> getProject(@PathVariable String id) {
        ProjectResponse response = projectService.getProject(id);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectResponse> updateProject(@PathVariable String id, @RequestBody ProjectUpdateRequest request) {
        ProjectResponse response = projectService.updateProject(id, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteProject(@PathVariable String id ) {
        projectService.deleteProject(id);
        return ApiResponse.success(null, "프로젝트가 삭제되었습니다.");
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectResponse> updateProjectStatus(@PathVariable String id, @RequestBody ProjectStatusUpdateRequest request) {
        ProjectResponse response = projectService.updateProjectStatus(id, request);
        return ApiResponse.success(response);
    }
}