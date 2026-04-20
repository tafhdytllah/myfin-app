package com.tafh.myfin_app.analytics.dto;

import com.tafh.myfin_app.category.model.CategoryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiggestTransactionResponse {

    private String transactionId;
    private String categoryName;
    private CategoryType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String description;
}
