package com.tafh.myfin_app.report.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.report.dto.MonthlyReportResponse;
import com.tafh.myfin_app.report.dto.ReportResponse;
import com.tafh.myfin_app.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<ReportResponse>> daily(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseHelper.ok(reportService.getDaily(accountId, date));
    }

    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<ReportResponse>> weekly(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseHelper.ok(reportService.getWeekly(accountId, date));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<List<MonthlyReportResponse>>> monthly(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseHelper.ok(reportService.getMonthly(accountId, year));
    }

}
