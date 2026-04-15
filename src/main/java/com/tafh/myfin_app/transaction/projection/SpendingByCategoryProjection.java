package com.tafh.myfin_app.transaction.projection;

import com.tafh.myfin_app.category.model.CategoryType;

import java.math.BigDecimal;

public interface SpendingByCategoryProjection {

    String getCategoryId();

    String getCategoryName();

    CategoryType getType();

    BigDecimal getTotal();
}
