package com.tafh.myfin_app.account.dto;


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
public class AccountRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Balance is required")
    @Positive(message = "Balance must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Invalid Balance format")
    private BigDecimal balance;



}
