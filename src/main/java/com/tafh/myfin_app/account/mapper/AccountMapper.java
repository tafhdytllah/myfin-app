package com.tafh.myfin_app.account.mapper;

import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.projection.AccountProjection;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toAccountResponse(AccountEntity account, long usageCount) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .openingBalance(account.getOpeningBalance())
                .currentBalance(account.getCurrentBalance())
                .active(account.isActive())
                .usageCount(usageCount)
                .build();
    }

    public AccountResponse toAccountResponse(AccountProjection account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .openingBalance(account.getOpeningBalance())
                .currentBalance(account.getCurrentBalance())
                .active(account.getActive())
                .usageCount(account.getUsageCount())
                .build();
    }

}
