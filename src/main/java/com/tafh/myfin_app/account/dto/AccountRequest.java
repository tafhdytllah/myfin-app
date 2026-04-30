package com.tafh.myfin_app.account.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Opening balance is required")
    @PositiveOrZero(message = "Opening balance cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Invalid Opening balance format")
    private BigDecimal openingBalance;

}
