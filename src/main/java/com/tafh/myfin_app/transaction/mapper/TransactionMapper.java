package com.tafh.myfin_app.transaction.mapper;

import com.tafh.myfin_app.transaction.dto.TransactionResponse;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toTransactionResponse(TransactionEntity transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .categoryId(transaction.getCategory().getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
