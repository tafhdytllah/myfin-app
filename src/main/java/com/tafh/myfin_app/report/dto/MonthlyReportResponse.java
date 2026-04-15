package com.tafh.myfin_app.report.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReportResponse {

    private String month;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;

}
