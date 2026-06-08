package com.example.devlog.dashboard;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.dashboard.dto.DashboardSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        DashboardSummaryResponse response = dashboardService.getSummary();
        return ApiResponse.success(response);
    }
}
