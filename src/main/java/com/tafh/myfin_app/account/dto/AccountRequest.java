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

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Invalid Balance format")
    private BigDecimal balance;

}
