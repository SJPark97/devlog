package com.example.devlog.dashboard.dto;

import com.example.devlog.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record DashboardSummaryResponse(
        @Schema(description = "활성 프로젝트 수", example = "2")
        long activeProjectCount,
        @Schema(description = "삭제되지 않은 전체 작업 수", example = "10")
        long totalTaskCount,
        @Schema(description = "작업 상태별 개수", example = "{\"TODO\":2,\"IN_PROGRESS\":1,\"DONE\":7,\"HOLD\":0,\"CANCELLED\":0}")
        Map<TaskStatus, Long> taskStatusCounts
) {
}
