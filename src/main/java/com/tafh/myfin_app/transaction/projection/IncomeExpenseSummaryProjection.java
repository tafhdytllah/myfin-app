package com.tafh.myfin_app.transaction.projection;

import java.math.BigDecimal;

public interface IncomeExpenseSummaryProjection {

    BigDecimal getIncome();

    BigDecimal getExpense();

}
