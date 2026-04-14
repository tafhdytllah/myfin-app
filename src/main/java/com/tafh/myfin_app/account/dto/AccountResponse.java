package com.tafh.myfin_app.account.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private String id;

    private String name;

    private BigDecimal balance;

}
