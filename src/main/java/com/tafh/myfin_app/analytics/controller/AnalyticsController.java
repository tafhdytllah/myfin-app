package com.tafh.myfin_app.analytics.controller;

import com.tafh.myfin_app.analytics.dto.BiggestTransactionResponse;
import com.tafh.myfin_app.analytics.dto.MonthlyTrendResponse;
import com.tafh.myfin_app.analytics.dto.SpendingByCategoryResponse;
import com.tafh.myfin_app.analytics.dto.SummaryResponse;
import com.tafh.myfin_app.analytics.service.AnalyticsService;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // 🟢 1
    @GetMapping("/spending-by-category")
    public ResponseEntity<ApiResponse<List<SpendingByCategoryResponse>>> spending(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        return ResponseHelper.ok(analyticsService.spendingByCategory(accountId, month));
    }

    // 🟢 2
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<SummaryResponse>> summary(
            @RequestParam(required = false) String accountId
    ) {
        return ResponseHelper.ok(analyticsService.summary(accountId));
    }

    // 🟢 3
    @GetMapping("/monthly-trend")
    public ResponseEntity<ApiResponse<List<MonthlyTrendResponse>>> monthlyTrend(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseHelper.ok(analyticsService.monthlyTrend(accountId, year));
    }

    // 🟢 4
    @GetMapping("/biggest-transaction")
    public ResponseEntity<ApiResponse<BiggestTransactionResponse>> biggestTransaction(
            @RequestParam(required = false) String accountId
    ) {
        return ResponseHelper.ok(analyticsService.biggestTransaction(accountId));
    }

}
