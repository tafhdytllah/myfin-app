package com.tafh.myfin_app.transaction.projection;

import java.math.BigDecimal;

public interface MonthlyTrendProjection {

    String getMonth();

    BigDecimal getIncome();

    BigDecimal getExpense();

}
