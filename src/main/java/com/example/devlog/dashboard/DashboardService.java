package com.example.devlog.dashboard;

import com.example.devlog.dashboard.dto.DashboardSummaryResponse;
import com.example.devlog.project.ProjectRepository;
import com.example.devlog.project.ProjectStatus;
import com.example.devlog.task.TaskRepository;
import com.example.devlog.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DashboardSummaryResponse getSummary() {
        long activeProjectCount = projectRepository.countByDeletedFalseAndStatus(ProjectStatus.ACTIVE);

        long totalTaskCount = 0;
        Map<TaskStatus, Long> taskStatusCounts = new EnumMap<>(TaskStatus.class);
        for (TaskStatus status : TaskStatus.values()) {
            taskStatusCounts.put(status, 0L);
        }
        List<Object[]> taskStatusList = taskRepository.countByStatusGroupByStatus();
        for (Object[] row : taskStatusList) {
            TaskStatus status = (TaskStatus) row[0];
            long count = (long) row[1];

            totalTaskCount += count;
            taskStatusCounts.put(status, count);
        }

        return new DashboardSummaryResponse(
                activeProjectCount,
                totalTaskCount,
                taskStatusCounts
        );
    }
}
