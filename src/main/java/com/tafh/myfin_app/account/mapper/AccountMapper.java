package com.tafh.myfin_app.account.mapper;

import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.model.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toAccountResponse(AccountEntity entity) {
        return AccountResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .balance(entity.getBalance())
                .build();
    }

}
