package com.example.devlog.project;

import com.example.devlog.common.deletion.DeletionValidator;
import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectFinder {
    private final ProjectRepository projectRepository;

    public Project getProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        DeletionValidator.validateNotDeleted(project.deletionCheck());
        return project;
    }
}
