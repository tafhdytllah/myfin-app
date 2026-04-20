package com.tafh.myfin_app.account.service;

import com.tafh.myfin_app.account.dto.AccountRequest;
import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.mapper.AccountMapper;
import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.security.CurrentUser;
import com.tafh.myfin_app.common.util.LikeQueryHelper;
import com.tafh.myfin_app.user.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CurrentUser currentUser;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AccountResponse create(AccountRequest request) {
        String userId = currentUser.getId();

        String name = request.getName().trim();

        UserEntity user = entityManager.getReference(UserEntity.class, userId);

        AccountEntity account = AccountEntity.create(
                user,
                name,
                request.getBalance()
        );

        accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccounts(
            String keyword,
            Pageable pageable
    ) {
        String userId = currentUser.getId();

        String searchTerm = LikeQueryHelper.toContainsPattern(keyword);

        Page<AccountEntity> accounts = accountRepository
                .findAllWithFilter(
                        userId,
                        searchTerm,
                        pageable
                );

        return accounts.map(accountMapper::toAccountResponse);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String id) {
        String userId = currentUser.getId();

        AccountEntity account = accountRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    public AccountResponse update(String id, AccountRequest request) {
        String userId = currentUser.getId();

        String name = request.getName().trim();

        AccountEntity account = accountRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        account.update(name);

        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    public void delete(String id) {
        String userId = currentUser.getId();

        AccountEntity account = accountRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        accountRepository.delete(account);
    }
}