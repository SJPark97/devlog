package com.example.devlog.task;

import com.example.devlog.project.Project;
import com.example.devlog.project.ProjectFinder;
import com.example.devlog.task.dto.TaskCreateRequest;
import com.example.devlog.task.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectFinder projectFinder;

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        Project project = projectFinder.getProjectById(request.getProjectId());
        Task task = Task.create(project, request.getTitle(), request.getContent());
        Task savedTask = taskRepository.save(task);
        return TaskResponse.from(savedTask);
    }
}
