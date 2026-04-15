package com.tafh.myfin_app.report.service;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.common.security.SecurityUtil;
import com.tafh.myfin_app.report.dto.MonthlyReportResponse;
import com.tafh.myfin_app.report.dto.ReportResponse;
import com.tafh.myfin_app.report.mapper.ReportMapper;
import com.tafh.myfin_app.transaction.projection.MonthlySummaryProjection;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;

    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public ReportResponse getDaily(String accountId, LocalDate date) {

        String userId = SecurityUtil.getCurrentUserId();

        if (date == null) {
            date = LocalDate.now();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        BigDecimal totalIncome = safe(
                transactionRepository.sumByTypeAndDateRange(
                        userId, accountId, CategoryType.INCOME, start, end)
        );

        BigDecimal totalExpense = safe(
                transactionRepository.sumByTypeAndDateRange(
                        userId, accountId, CategoryType.EXPENSE, start, end)
        );

        return ReportMapper.toReportResponse(
                date.toString(),
                totalIncome,
                totalExpense,
                totalIncome.subtract(totalExpense)
        );

    }

    @Transactional(readOnly = true)
    public ReportResponse getWeekly(String accountId, LocalDate date) {

        String userId = SecurityUtil.getCurrentUserId();

        if (date == null) {
            date = LocalDate.now();
        }

        LocalDate startDate = date.with(DayOfWeek.MONDAY);
        LocalDate endDate = date.with(DayOfWeek.SUNDAY);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        BigDecimal totalIncome = safe(
                transactionRepository.sumByTypeAndDateRange(
                        userId, accountId, CategoryType.INCOME, start, end)
        );

        BigDecimal totalExpense = safe(
                transactionRepository.sumByTypeAndDateRange(
                        userId, accountId, CategoryType.EXPENSE, start, end)
        );

        return ReportMapper.toReportResponse(
                String.format("%s to %s", startDate, endDate),
                totalIncome,
                totalExpense,
                totalIncome.subtract(totalExpense)
        );
    }

    @Transactional(readOnly = true)
    public List<MonthlyReportResponse> getMonthly(String accountId, Integer year) {
        String userId = SecurityUtil.getCurrentUserId();

        if (year == null) {
            year = Year.now().getValue();
        }

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<MonthlySummaryProjection> result = transactionRepository.monthlySummary(userId, accountId, start, end);

        return result.stream().map(r -> {
            BigDecimal totalIncome = r.getTotalIncome() != null
                    ? r.getTotalIncome()
                    : BigDecimal.ZERO;

            BigDecimal totalExpense = r.getTotalExpense() != null
                    ? r.getTotalExpense()
                    : BigDecimal.ZERO;

            BigDecimal balance = totalIncome.subtract(totalExpense);

            return ReportMapper.toMonthlyResponse(
                    r.getMonth(),
                    totalIncome,
                    totalExpense,
                    balance
            );
        }).toList();
    }

}
