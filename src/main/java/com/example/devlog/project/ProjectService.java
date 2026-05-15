package com.example.devlog.project;

import com.example.devlog.project.dto.ProjectCreateRequest;
import com.example.devlog.project.dto.ProjectResponse;
import com.example.devlog.project.dto.ProjectStatusUpdateRequest;
import com.example.devlog.project.dto.ProjectUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFinder projectFinder;

    public ProjectService(ProjectRepository projectRepository, ProjectFinder projectFinder) {
        this.projectRepository = projectRepository;
        this.projectFinder = projectFinder;
    }

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