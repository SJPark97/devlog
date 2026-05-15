package com.example.devlog.project;

import com.example.devlog.project.dto.ProjectCreateRequest;
import com.example.devlog.project.dto.ProjectResponse;
import com.example.devlog.project.dto.ProjectStatusUpdateRequest;
import com.example.devlog.project.dto.ProjectUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFinder projectFinder;

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request) {
        Project project = Project.create(
                request.getName(),
                request.getDescription()
        );

        Project savedProject = projectRepository.save(project);

        return ProjectResponse.from(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects() {
        return projectRepository.findByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjectsPage(
            int page,
            int size,
            String sortBy,
            String direction,
            String keyword
    ) {
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        if (keyword != null && !keyword.isBlank()) {
            return projectRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageRequest)
                    .map(ProjectResponse::from);
        }

        return projectRepository.findByDeletedFalse(pageRequest)
                .map(ProjectResponse::from);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(String id) {
        Project project = projectFinder.getProjectById(id);
        return ProjectResponse.from(project);
    }

    @Transactional
    public ProjectResponse updateProject(String id, ProjectUpdateRequest request) {
        Project project = projectFinder.getProjectById(id);
        project.update(request.getName(), request.getDescription());
        return ProjectResponse.from(project);
    }

    @Transactional
    public void deleteProject(String id) {
        Project project = projectFinder.getProjectById(id);
        project.delete();
    }

    @Transactional
    public ProjectResponse updateProjectStatus(String id, ProjectStatusUpdateRequest request) {
        Project project = projectFinder.getProjectById(id);
        project.updateStatus(request.getStatus());
        return ProjectResponse.from(project);
    }
}