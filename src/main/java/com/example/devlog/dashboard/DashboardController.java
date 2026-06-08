package com.example.devlog.dashboard;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.dashboard.dto.DashboardSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "대시보드 API")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "대시보드 요약 조회", description = "프로젝트와 작업 상태 기준의 대시보드 요약 정보를 조회합니다.")
    @GetMapping("/summary")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        DashboardSummaryResponse response = dashboardService.getSummary();
        return ApiResponse.success(response);
    }
}
