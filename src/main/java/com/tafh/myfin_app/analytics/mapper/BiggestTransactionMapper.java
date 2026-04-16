package com.tafh.myfin_app.analytics.mapper;

import com.tafh.myfin_app.analytics.dto.BiggestTransactionResponse;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class BiggestTransactionMapper {

    public BiggestTransactionResponse toBiggestTransactionResponse(TransactionEntity trx) {
        return BiggestTransactionResponse.builder()
                .transactionId(trx.getId())
                .categoryName(trx.getCategory().getName())
                .type(trx.getCategory().getType())
                .amount(trx.getAmount())
                .createdAt(trx.getCreatedAt())
                .description(trx.getDescription())
                .build();
    }

}
