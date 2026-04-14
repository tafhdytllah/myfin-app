package com.tafh.myfin_app.transaction.service;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.PagingResponse;
import com.tafh.myfin_app.common.exception.BadRequestException;
import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.security.SecurityUtil;
import com.tafh.myfin_app.common.util.LogHelper;
import com.tafh.myfin_app.transaction.dto.TransactionRequest;
import com.tafh.myfin_app.transaction.dto.TransactionResponse;
import com.tafh.myfin_app.transaction.dto.TransactionSummaryResponse;
import com.tafh.myfin_app.transaction.enums.TransactionType;
import com.tafh.myfin_app.transaction.mapper.TransactionMapper;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        String userId = SecurityUtil.getCurrentUserId();

        AccountEntity account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("amount", "Amount must be greather than zero");
        }

        TransactionEntity trx = TransactionEntity.create(
                account,
                request.getAmount(),
                request.getType(),
                request.getDescription()
        );

        trx = transactionRepository.save(trx);

        return transactionMapper.toTransactionResponse(trx);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<TransactionResponse>> getAll(String accountId, Pageable pageable) {
        String userId = SecurityUtil.getCurrentUserId();

        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Page<TransactionEntity> page = transactionRepository.findByAccountId(account.getId(), pageable);

        List<TransactionResponse> data = page
                .map(transactionMapper::toTransactionResponse)
                .getContent();

        PagingResponse paging = PagingResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        return ApiResponse.success(data, paging);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(String id) {
        String userId = SecurityUtil.getCurrentUserId();

        TransactionEntity trx = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return transactionMapper.toTransactionResponse(trx);
    }

    @Transactional
    public void delete(String id) {
        String userId = SecurityUtil.getCurrentUserId();

        TransactionEntity trx = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        LogHelper.warn("TRX {}", trx);
        if (trx.getType() == TransactionType.INCOME) {
            LogHelper.warn("MASUK DECREASE");
            trx.getAccount().decreaseBalance(trx.getAmount());
            LogHelper.warn("MASUK DECREASE END");
        }  else {
            LogHelper.warn("MASUK INCREASE");
            trx.getAccount().increaseBalance(trx.getAmount());
            LogHelper.warn("MASUK INCREASE END");
        }
        transactionRepository.delete(trx);
    }

    @Transactional(readOnly = true)
    public TransactionSummaryResponse getSummary(String accountId) {
        String userId = SecurityUtil.getCurrentUserId();

        BigDecimal totalIncome = transactionRepository.sumByAccountAndUserAndType(userId, accountId, TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByAccountAndUserAndType(userId, accountId, TransactionType.EXPENSE);

        return TransactionSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();
    }
}
