package com.tafh.myfin_app.transaction.dto;


import com.tafh.myfin_app.transaction.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private String id;

    private String accountId;

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private LocalDateTime createdAt;

}
