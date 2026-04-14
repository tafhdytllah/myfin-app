package com.tafh.myfin_app.account.dto;


import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Balance is required")
    private BigDecimal balance;

}
