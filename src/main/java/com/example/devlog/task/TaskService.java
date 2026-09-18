package com.example.devlog.task;

import com.example.devlog.common.event.ProjectEvent;
import com.example.devlog.common.event.ProjectEventType;
import com.example.devlog.project.Project;
import com.example.devlog.project.ProjectFinder;
import com.example.devlog.task.dto.TaskCreateRequest;
import com.example.devlog.task.dto.TaskResponse;
import com.example.devlog.task.dto.TaskStatusUpdateRequest;
import com.example.devlog.task.dto.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectFinder projectFinder;
    private final TaskFinder taskFinder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        Project project = projectFinder.getProjectById(request.projectId());
        Task task = Task.create(project, request.title(), request.content());
        Task savedTask = taskRepository.save(task);
        TaskResponse response = TaskResponse.from(savedTask);
        eventPublisher.publishEvent(new ProjectEvent(project.getId(), ProjectEventType.TASK_CREATED, response));
        return response;
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

    @Transactional
    public TaskResponse updateTask(String id, TaskUpdateRequest request) {
        Task task = taskFinder.getTaskById(id);
        task.update(request.title(), request.content());
        TaskResponse response = TaskResponse.from(task);

        eventPublisher.publishEvent(new ProjectEvent(response.projectId(), ProjectEventType.TASK_UPDATED, response));
        return response;
    }

    @Transactional
    public TaskResponse updateTaskStatus(String id, TaskStatusUpdateRequest request) {
        Task task = taskFinder.getTaskById(id);
        task.updateStatus(request.status());
        return TaskResponse.from(task);
    }

    @Transactional
    public void deleteTask(String id) {
        Task task = taskFinder.getTaskById(id);
        task.delete();
    }

}
