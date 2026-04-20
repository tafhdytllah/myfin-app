package com.tafh.myfin_app.dashboard_summary.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.dashboard_summary.dto.DashboardSummaryResponse;
import com.tafh.myfin_app.dashboard_summary.service.DashboardSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardSummaryController {

    private final DashboardSummaryService dashboardSummaryService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getGlobalSummary() {
        return ResponseHelper.ok(dashboardSummaryService.getGlobalSummary());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary(@PathVariable String accountId) {
        return ResponseHelper.ok(dashboardSummaryService.getSummary(accountId));
    }
}
