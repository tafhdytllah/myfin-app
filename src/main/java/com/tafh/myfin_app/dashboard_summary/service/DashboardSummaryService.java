package com.tafh.myfin_app.dashboard_summary.service;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.common.security.SecurityUtil;
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


    @Transactional(readOnly = true)
    public DashboardSummaryResponse getGlobalDashboard() {
        String userId = SecurityUtil.getCurrentUserId();

        BigDecimal totalIncome = transactionRepository.sumByUserAndType(userId, CategoryType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByUserAndType(userId, CategoryType.EXPENSE);

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();

    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboard(String id) {
        String userId = SecurityUtil.getCurrentUserId();

        BigDecimal totalIncome = transactionRepository.sumByAccountAndUserAndType(id, userId, CategoryType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByAccountAndUserAndType(id, userId, CategoryType.EXPENSE);

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();

    }


}
