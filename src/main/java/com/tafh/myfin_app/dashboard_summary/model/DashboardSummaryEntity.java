package com.tafh.myfin_app.dashboard_summary.model;

import com.tafh.myfin_app.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "dashboard_summary")
@Getter
@NoArgsConstructor
public class DashboardSummaryEntity extends BaseEntity {

    @Column(name = "total_income", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalIncome;

    @Column(name = "total_expense", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalExpense;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(length = 7, nullable = false)
    private String period;

    @Column(name = "user_id", length = 64, nullable = false)
    private String userId;

    @Column(name = "account_id", length = 64)
    private String accountId;

}
