package com.tafh.myfin_app.report.mapper;

import com.tafh.myfin_app.report.dto.MonthlyReportResponse;
import com.tafh.myfin_app.report.dto.ReportResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReportMapper {

    public static ReportResponse toReportResponse(
            String period,
            BigDecimal totalIncome,
            BigDecimal totalExpense,
            BigDecimal balance
    ) {
        return ReportResponse.builder()
                .period(period)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    public static MonthlyReportResponse toMonthlyResponse(
            String month,
            BigDecimal totalIncome,
            BigDecimal totalExpense,
            BigDecimal balance
    ) {
        return MonthlyReportResponse.builder()
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

}
