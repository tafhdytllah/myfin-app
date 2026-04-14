package com.tafh.myfin_app.transaction.mapper;

import com.tafh.myfin_app.transaction.dto.TransactionResponse;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toTransactionResponse(TransactionEntity trx) {
        return TransactionResponse.builder()
                .id(trx.getId())
                .accountId(trx.getAccount().getId())
                .amount(trx.getAmount())
                .type(trx.getType())
                .description(trx.getDescription())
                .createdAt(trx.getCreatedAt())
                .build();
    }

}
