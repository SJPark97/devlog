package com.example.devlog.dashboard.dto;

import com.example.devlog.task.TaskStatus;

import java.util.Map;

public record DashboardSummaryResponse(
        long activeProjectCount,
        long totalTaskCount,
        Map<TaskStatus, Long> taskStatusCounts,
        long recentCommentCount
) {
}
