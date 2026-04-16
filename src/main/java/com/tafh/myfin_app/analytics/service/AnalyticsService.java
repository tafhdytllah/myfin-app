package com.tafh.myfin_app.analytics.service;

import com.tafh.myfin_app.analytics.dto.BiggestTransactionResponse;
import com.tafh.myfin_app.analytics.dto.MonthlyTrendResponse;
import com.tafh.myfin_app.analytics.dto.SpendingByCategoryResponse;
import com.tafh.myfin_app.analytics.dto.IncomeExpenseSummaryResponse;
import com.tafh.myfin_app.analytics.mapper.BiggestTransactionMapper;
import com.tafh.myfin_app.common.security.SecurityHelper;
import com.tafh.myfin_app.common.util.DateRangeHelper;
import com.tafh.myfin_app.common.util.NumberHelper;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.projection.MonthlyTrendProjection;
import com.tafh.myfin_app.transaction.projection.SpendingByCategoryProjection;
import com.tafh.myfin_app.transaction.projection.IncomeExpenseSummaryProjection;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final BiggestTransactionMapper biggestTransactionMapper;

    @Transactional(readOnly = true)
    public List<SpendingByCategoryResponse> getSpendingByCategory(
            String accountId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        String userId = SecurityHelper.getCurrentUserId();

        DateRangeHelper.DateTimeRange rangeDateTime = DateRangeHelper.toDateTimeRange(startDate, endDate);

        List<SpendingByCategoryProjection> rows = transactionRepository
                .getSpendingByCategory(
                        userId,
                        accountId,
                        rangeDateTime.startDateTime(),
                        rangeDateTime.endDateTime()
                );

        return rows.stream()
                .map(row ->
                        SpendingByCategoryResponse.builder()
                                .categoryId(row.getCategoryId())
                                .categoryName(row.getCategoryName())
                                .type(row.getType())
                                .total(row.getTotal() != null ? row.getTotal() : BigDecimal.ZERO)
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

        IncomeExpenseSummaryProjection summary = transactionRepository
                .getIncomeExpenseSummary(
                        userId,
                        accountId,
                        rangeDateTime.startDateTime(),
                        rangeDateTime.endDateTime()
                );

        BigDecimal income = NumberHelper.zeroIfNull(summary.getIncome());
        BigDecimal expense = NumberHelper.zeroIfNull(summary.getExpense());

        return IncomeExpenseSummaryResponse.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .balance(income.subtract(expense))
                .build();
    }

    @Transactional(readOnly = true)
    public List<MonthlyTrendResponse> getMonthlyTrend(
            String accountId,
            Integer year
    ) {

        String userId = SecurityHelper.getCurrentUserId();

        int targetYear = DateRangeHelper.resolveYearOrCurrent(year);

        LocalDateTime startDateTime = LocalDate.of(targetYear, 1, 1).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.of(targetYear, 12, 31).atTime(LocalTime.MAX);

        List<MonthlyTrendProjection> rows = transactionRepository
                .getMonthlyTrend(
                        userId,
                        accountId,
                        startDateTime,
                        endDateTime
                );

        return rows.stream().map(row -> {
            BigDecimal income = NumberHelper.zeroIfNull(row.getIncome());
            BigDecimal expense = NumberHelper.zeroIfNull(row.getExpense());

            return MonthlyTrendResponse.builder()
                    .month(row.getMonth())
                    .totalIncome(income)
                    .totalExpense(expense)
                    .balance(income.subtract(expense))
                    .build();
        }).toList();
    }

    @Transactional(readOnly = true)
    public Page<BiggestTransactionResponse> biggestTransaction(
            String accountId,
            LocalDate startDate,
            LocalDate endDate,
            int limit
    ) {

        String userId = SecurityHelper.getCurrentUserId();

        DateRangeHelper.DateTimeRange rangeDateTime = DateRangeHelper.toDateTimeRange(startDate, endDate);

        Pageable pageable = PageRequest.of(0, limit);

        Page<TransactionEntity> page = transactionRepository
                .findBiggestTransaction(
                        userId,
                        accountId,
                        rangeDateTime.startDateTime(),
                        rangeDateTime.endDateTime(),
                        pageable
                );

        return page.map(biggestTransactionMapper::toBiggestTransactionResponse);
    }

}
