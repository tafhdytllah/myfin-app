package com.tafh.myfin_app.transaction.projection;

import java.math.BigDecimal;

public interface SummaryProjection {

    BigDecimal getIncome();

    BigDecimal getExpense();

}
