package com.tafh.myfin_app.analytics.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyTrendResponse {

    private String month;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;

}
