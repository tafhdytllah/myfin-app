package com.tafh.myfin_app.dashboard_summary.service;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.common.security.CurrentUser;
import com.tafh.myfin_app.dashboard_summary.dto.DashboardSummaryResponse;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardSummaryService {

    private final TransactionRepository transactionRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getGlobalSummary() {
        String userId = currentUser.getId();

        BigDecimal totalIncome = transactionRepository
                .sumByUserAndType(
                        userId,
                        CategoryType.INCOME
                );
        BigDecimal totalExpense = transactionRepository
                .sumByUserAndType(
                        userId,
                        CategoryType.EXPENSE
                );

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary(String accountId) {
        String userId = currentUser.getId();

        BigDecimal totalIncome = transactionRepository
                .sumByAccountAndUserAndType(
                        accountId,
                        userId,
                        CategoryType.INCOME
                );
        BigDecimal totalExpense = transactionRepository
                .sumByAccountAndUserAndType(
                        accountId,
                        userId,
                        CategoryType.EXPENSE
                );

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();

    }
}
