package com.example.devlog.task;

import com.example.devlog.project.Project;
import com.example.devlog.project.ProjectFinder;
import com.example.devlog.task.dto.TaskCreateRequest;
import com.example.devlog.task.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectFinder projectFinder;
    private final TaskFinder taskFinder;

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        Project project = projectFinder.getProjectById(request.getProjectId());
        Task task = Task.create(project, request.getTitle(), request.getContent());
        Task savedTask = taskRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(String projectId) {
        List<Task> tasks = taskRepository.findByProjectIdAndDeletedFalseOrderByCreatedAtDesc(projectId);

        return tasks.stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(String id) {
        Task task = taskFinder.getTaskById(id);
        return TaskResponse.from(task);
    }

}
