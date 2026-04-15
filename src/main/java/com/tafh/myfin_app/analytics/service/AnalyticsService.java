package com.tafh.myfin_app.analytics.service;

import com.tafh.myfin_app.analytics.dto.BiggestTransactionResponse;
import com.tafh.myfin_app.analytics.dto.MonthlyTrendResponse;
import com.tafh.myfin_app.analytics.dto.SpendingByCategoryResponse;
import com.tafh.myfin_app.analytics.dto.SummaryResponse;
import com.tafh.myfin_app.common.security.SecurityUtil;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.projection.MonthlyTrendProjection;
import com.tafh.myfin_app.transaction.projection.SpendingByCategoryProjection;
import com.tafh.myfin_app.transaction.projection.SummaryProjection;
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

        String userId = SecurityUtil.getCurrentUserId();

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
    public SummaryResponse summary(String accountId) {

        String userId = SecurityUtil.getCurrentUserId();

        SummaryProjection row = transactionRepository.summary(userId, accountId);

        BigDecimal income = row.getIncome() != null ? row.getIncome() : BigDecimal.ZERO;
        BigDecimal expense = row.getExpense() != null ? row.getExpense() : BigDecimal.ZERO;

        return SummaryResponse.builder()
                .income(income)
                .expense(expense)
                .balance(income.subtract(expense))
                .build();
    }

    @Transactional(readOnly = true)
    public List<MonthlyTrendResponse> monthlyTrend(String accountId, Integer year) {

        String userId = SecurityUtil.getCurrentUserId();

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

        String userId = SecurityUtil.getCurrentUserId();

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
