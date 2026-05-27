package com.tafh.myfin_app.account.service;

import com.tafh.myfin_app.account.dto.AccountRequest;
import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.dto.StatusAccountRequest;
import com.tafh.myfin_app.account.dto.UpdateAccountRequest;
import com.tafh.myfin_app.account.mapper.AccountMapper;
import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.projection.AccountProjection;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.category.dto.StatusCategoryRequest;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.projection.CategoryProjection;
import com.tafh.myfin_app.common.exception.BadRequestException;
import com.tafh.myfin_app.common.exception.DomainException;
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

        if (accountRepository.existsByUser_IdAndNameIgnoreCase(user.getId(), name)) {
            throw new BadRequestException("name", "Account name already exists");
        }

        AccountEntity account = AccountEntity.create(
                user,
                name,
                request.getOpeningBalance()
        );

        accountRepository.save(account);

        return accountMapper.toAccountResponse(account, 0L);
    }

    @Transactional
    public AccountResponse update(String id, UpdateAccountRequest request) {
        String userId = currentUser.getId();

        String name = request.getName().trim();

        AccountEntity account = accountRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        if (accountRepository.existsByUser_IdAndNameIgnoreCase(userId, name)) {
            throw new BadRequestException("name", "Account name already exists");
        }

        account.update(name);

        AccountProjection accountDetail = accountRepository
                .findDetail(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        return accountMapper.toAccountResponse(accountDetail);
    }

    @Transactional
    public AccountResponse updateStatus(String id, StatusAccountRequest request) {
        String userId = currentUser.getId();

        AccountEntity accountEntity = accountRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        if (request.getActive() == true) {
            accountEntity.active();
        } else {
            accountEntity.deactivate();
        }

        AccountProjection accountDetail = accountRepository
                .findDetail(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        return accountMapper.toAccountResponse(accountDetail);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccounts(
            Boolean active,
            String keyword,
            Pageable pageable
    ) {
        String userId = currentUser.getId();

        String searchTerm = LikeQueryHelper.toContainsPattern(keyword);

        Page<AccountProjection> accounts = accountRepository
                .findAllWithFilter(
                        userId,
                        active,
                        searchTerm,
                        pageable
                );

        return accounts.map(accountMapper::toAccountResponse);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String id) {
        String userId = currentUser.getId();

        AccountProjection account = accountRepository
                .findDetail(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

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