package com.tafh.myfin_app.transaction.projection;

import java.math.BigDecimal;

public interface MonthlySummaryProjection {

    String getMonth();

    BigDecimal getTotalIncome();

    BigDecimal getTotalExpense();

}
