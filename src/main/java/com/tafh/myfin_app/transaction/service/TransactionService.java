package com.tafh.myfin_app.transaction.service;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.category.repository.CategoryRepository;
import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.model.DateTimeRange;
import com.tafh.myfin_app.common.security.CurrentUser;
import com.tafh.myfin_app.common.util.DateRangeHelper;
import com.tafh.myfin_app.common.util.LikeQueryHelper;
import com.tafh.myfin_app.transaction.dto.TransactionRequest;
import com.tafh.myfin_app.transaction.dto.TransactionResponse;
import com.tafh.myfin_app.transaction.dto.TransactionSummaryResponse;
import com.tafh.myfin_app.transaction.mapper.TransactionMapper;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final CategoryRepository categoryRepository;
    private final CurrentUser currentUser;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        String userId = currentUser.getId();

        String description = Optional.ofNullable(request.getDescription()).map(String::trim).orElse(null);
        BigDecimal amount = request.getAmount();

        AccountEntity account = accountRepository
                .findByIdAndUser_Id(request.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        CategoryEntity category = categoryRepository
                .findByIdAndUser_Id(request.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        if (request.getType() == CategoryType.INCOME) {
            account.increaseBalance(amount);
        } else {
            account.decreaseBalance(amount);
        }

        TransactionEntity transaction = TransactionEntity.create(
                account,
                category,
                request.getAmount(),
                request.getType(),
                description
        );

        transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(
            String accountId,
            CategoryType type,
            String categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Pageable pageable
    ) {
        String userId = currentUser.getId();

        DateTimeRange rangeDateTime = DateRangeHelper.toDateTimeRange(startDate, endDate);
        String searchTerm = LikeQueryHelper.toContainsPattern(keyword);

        Page<TransactionEntity> page = transactionRepository
                .findAllWithFilter(
                        userId,
                        accountId,
                        type,
                        categoryId,
                        rangeDateTime.getStartDateTime(),
                        rangeDateTime.getEndDateTime(),
                        searchTerm,
                        pageable
                );

        return page.map(transactionMapper::toTransactionResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(String id) {
        String userId = currentUser.getId();

        TransactionEntity transaction = transactionRepository
                .findByIdAndAccount_User_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction is not found"));

        return transactionMapper.toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionSummaryResponse getSummary(String accountId) {
        String userId = currentUser.getId();

        BigDecimal totalIncome = transactionRepository.sumByAccountAndUserAndType(accountId, userId, CategoryType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByAccountAndUserAndType(accountId, userId, CategoryType.EXPENSE);

        return TransactionSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();
    }

    @Transactional
    public void delete(String id) {
        String userId = currentUser.getId();

        TransactionEntity transaction = transactionRepository
                .findByIdAndAccount_User_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction is not found"));

        if (transaction.getType() == CategoryType.INCOME) {
            transaction.getAccount().decreaseBalance(transaction.getAmount());
        } else {
            transaction.getAccount().increaseBalance(transaction.getAmount());
        }

        transactionRepository.delete(transaction);
    }

}
