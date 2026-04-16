package com.tafh.myfin_app.analytics.service;

import com.tafh.myfin_app.analytics.dto.BiggestTransactionResponse;
import com.tafh.myfin_app.analytics.dto.MonthlyTrendResponse;
import com.tafh.myfin_app.analytics.dto.SpendingByCategoryResponse;
import com.tafh.myfin_app.analytics.dto.IncomeExpenseSummaryResponse;
import com.tafh.myfin_app.common.security.SecurityHelper;
import com.tafh.myfin_app.common.util.DateRangeHelper;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.projection.MonthlyTrendProjection;
import com.tafh.myfin_app.transaction.projection.SpendingByCategoryProjection;
import com.tafh.myfin_app.transaction.projection.IncomeExpenseSummaryProjection;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<SpendingByCategoryResponse> spendingByCategory(
            String accountId,
            YearMonth month
    ) {

        String userId = SecurityHelper.getCurrentUserId();

        LocalDateTime start;
        LocalDateTime end;

        if (month != null) {
            start = month.atDay(1).atStartOfDay();
            end = month.atEndOfMonth().atTime(LocalTime.MAX);
        } else {
            start = LocalDateTime.of(1970, 1, 1, 0, 0);
            end = LocalDateTime.now();
        }

        List<SpendingByCategoryProjection> rows = transactionRepository
                .spendingByCategory(userId, accountId, start, end);

        return rows.stream().map(r -> SpendingByCategoryResponse.builder()
                .categoryId(r.getCategoryId())
                .categoryName(r.getCategoryName())
                .type(r.getType())
                .total(r.getTotal() != null ? r.getTotal() : BigDecimal.ZERO)
                .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    public IncomeExpenseSummaryResponse getIncomeExpenseSummary(
            String accountId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        String userId = SecurityHelper.getCurrentUserId();

        DateRangeHelper.DateTimeRange rangeDateTime = DateRangeHelper.toDateTimeRange(startDate, endDate);

        IncomeExpenseSummaryProjection summary = transactionRepository.getIncomeExpenseSummary(
                userId,
                accountId,
                rangeDateTime.startDateTime(),
                rangeDateTime.endDateTime()
        );

        BigDecimal income = summary.getIncome() != null ? summary.getIncome() : BigDecimal.ZERO;
        BigDecimal expense = summary.getExpense() != null ? summary.getExpense() : BigDecimal.ZERO;

        return IncomeExpenseSummaryResponse.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .balance(income.subtract(expense))
                .build();
    }

    @Transactional(readOnly = true)
    public List<MonthlyTrendResponse> monthlyTrend(String accountId, Integer year) {

        String userId = SecurityHelper.getCurrentUserId();

        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);

        List<MonthlyTrendProjection> rows =
                transactionRepository.monthlyTrend(userId, accountId, start, end);

        return rows.stream().map(r -> {
            BigDecimal income = r.getIncome() != null ? r.getIncome() : BigDecimal.ZERO;
            BigDecimal expense = r.getExpense() != null ? r.getExpense() : BigDecimal.ZERO;

            return MonthlyTrendResponse.builder()
                    .month(r.getMonth())
                    .income(income)
                    .expense(expense)
                    .balance(income.subtract(expense))
                    .build();
        }).toList();
    }

    @Transactional(readOnly = true)
    public BiggestTransactionResponse biggestTransaction(String accountId) {

        String userId = SecurityHelper.getCurrentUserId();

        List<TransactionEntity> list =
                transactionRepository.findBiggest(userId, accountId);

        if (list.isEmpty()) {
            return null;
        }

        TransactionEntity t = list.get(0);

        return BiggestTransactionResponse.builder()
                .transactionId(t.getId())
                .categoryName(t.getCategory().getName())
                .type(t.getCategory().getType())
                .amount(t.getAmount())
                .description(t.getDescription())
                .build();
    }

}
