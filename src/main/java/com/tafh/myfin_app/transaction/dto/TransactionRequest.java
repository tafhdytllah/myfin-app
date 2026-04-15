package com.tafh.myfin_app.transaction.dto;

import com.tafh.myfin_app.category.model.CategoryType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    @NotBlank(message = "Account id is required")
    private String accountId;

    @NotBlank(message = "Category id is required")
    private String categoryId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private CategoryType type;

    private String description;

}
