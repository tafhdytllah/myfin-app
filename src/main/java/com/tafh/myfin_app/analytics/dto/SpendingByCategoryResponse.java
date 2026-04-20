package com.tafh.myfin_app.analytics.dto;

import com.tafh.myfin_app.category.model.CategoryType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpendingByCategoryResponse {

    private String categoryId;
    private String categoryName;
    private CategoryType type;
    private BigDecimal total;
}
