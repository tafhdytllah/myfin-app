package com.tafh.myfin_app.transaction.service;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.category.repository.CategoryRepository;
import com.tafh.myfin_app.common.exception.BadRequestException;
import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.security.SecurityHelper;
import com.tafh.myfin_app.common.util.DateRangeHelper;
import com.tafh.myfin_app.common.util.LikeQueryHelper;
import com.tafh.myfin_app.common.util.LogHelper;
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
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        String userId = SecurityHelper.getCurrentUserId();

        AccountEntity account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        CategoryEntity category = categoryRepository.findByIdAndUserId(request.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("amount", "Amount must be greather than zero");
        }

        TransactionEntity trx = TransactionEntity.create(
                account,
                category,
                request.getAmount(),
                request.getType(),
                request.getDescription()
        );

        trx = transactionRepository.save(trx);

        return transactionMapper.toTransactionResponse(trx);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAll(
            String accountId,
            CategoryType type,
            String categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Pageable pageable
    ) {
        String userId = SecurityHelper.getCurrentUserId();

        DateRangeHelper.DateTimeRange rangeDateTime = DateRangeHelper.toDateTimeRange(startDate, endDate);

        String searchTerm = LikeQueryHelper.toContainsPattern(keyword);

        Page<TransactionEntity> page = transactionRepository
                .findAllWithFilter(
                        userId,
                        accountId,
                        type,
                        categoryId,
                        rangeDateTime.startDateTime(),
                        rangeDateTime.endDateTime(),
                        searchTerm,
                        pageable
                );

        return page.map(transactionMapper::toTransactionResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(String id) {
        String userId = SecurityHelper.getCurrentUserId();

        TransactionEntity trx = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return transactionMapper.toTransactionResponse(trx);
    }

    @Transactional
    public void delete(String id) {
        String userId = SecurityHelper.getCurrentUserId();

        TransactionEntity trx = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (trx.getType() == CategoryType.INCOME) {
            trx.getAccount().decreaseBalance(trx.getAmount());
        } else {
            trx.getAccount().increaseBalance(trx.getAmount());
        }
        transactionRepository.delete(trx);
    }

    @Transactional(readOnly = true)
    public TransactionSummaryResponse getSummary(String accountId) {
        String userId = SecurityHelper.getCurrentUserId();

        BigDecimal totalIncome = transactionRepository.sumByAccountAndUserAndType(accountId, userId, CategoryType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByAccountAndUserAndType(accountId, userId, CategoryType.EXPENSE);

        return TransactionSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();
    }
}
