package com.tafh.myfin_app.analytics.controller;

import com.tafh.myfin_app.analytics.dto.BiggestTransactionResponse;
import com.tafh.myfin_app.analytics.dto.MonthlyTrendResponse;
import com.tafh.myfin_app.analytics.dto.SpendingByCategoryResponse;
import com.tafh.myfin_app.analytics.dto.IncomeExpenseSummaryResponse;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;


    @GetMapping("/spending-by-category")
    public ResponseEntity<ApiResponse<List<SpendingByCategoryResponse>>> spending(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseHelper.ok(
                analyticsService.getSpendingByCategory(
                        accountId,
                        startDate,
                        endDate
                )
        );
    }


    @GetMapping("/income-expense-summary")
    public ResponseEntity<ApiResponse<IncomeExpenseSummaryResponse>> summary(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseHelper.ok(
                analyticsService.getIncomeExpenseSummary(
                        accountId,
                        startDate,
                        endDate
                )
        );
    }


    @GetMapping("/monthly-trend")
    public ResponseEntity<ApiResponse<List<MonthlyTrendResponse>>> monthlyTrend(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseHelper.ok(analyticsService.getMonthlyTrend(accountId, year));
    }


    @GetMapping("/biggest-transaction")
    public ResponseEntity<ApiResponse<BiggestTransactionResponse>> biggestTransaction(
            @RequestParam(required = false) String accountId
    ) {
        return ResponseHelper.ok(analyticsService.biggestTransaction(accountId));
    }

}
