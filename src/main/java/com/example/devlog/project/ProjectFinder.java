package com.example.devlog.project;

import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ProjectFinder {
    private final ProjectRepository projectRepository;

    public ProjectFinder(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    public Project getProjectById(String id) {
        return projectRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
    }
}
