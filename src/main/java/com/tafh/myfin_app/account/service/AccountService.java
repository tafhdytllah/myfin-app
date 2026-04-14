package com.tafh.myfin_app.account.service;

import com.tafh.myfin_app.account.dto.AccountRequest;
import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.mapper.AccountMapper;
import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.SecurityUtil;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse create(AccountRequest request) {
        String userId = SecurityUtil.getCurrentUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        AccountEntity account = AccountEntity.create(
                user,
                request.getName(),
                request.getBalance()
        );

        accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> findAll() {
        String userId = SecurityUtil.getCurrentUserId();

        return accountRepository.findAllByUserId(userId)
                .stream()
                .map(accountMapper::toAccountResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse findById(String id) {
        String userId = SecurityUtil.getCurrentUserId();

        AccountEntity account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new UnauthorizedException("Account not found"));

        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    public AccountResponse update(String id, AccountRequest request) {
        String userId = SecurityUtil.getCurrentUserId();

        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User not authorized");
        }

        account.updateName(request.getName());

        accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    public void delete(String id) {
        String userId = SecurityUtil.getCurrentUserId();

        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Account not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User not authorized");
        }

        accountRepository.delete(account);
    }
}































