package com.tafh.myfin_app.transaction.controller;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.MetaMapper;
import com.tafh.myfin_app.common.dto.MetaResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.transaction.dto.TransactionRequest;
import com.tafh.myfin_app.transaction.dto.TransactionResponse;
import com.tafh.myfin_app.transaction.dto.TransactionSummaryResponse;
import com.tafh.myfin_app.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final MetaMapper metaMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseHelper.created(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) CategoryType type,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<TransactionResponse> page = transactionService.getTransactions(
                accountId,
                type,
                categoryId,
                startDate,
                endDate,
                keyword,
                pageable
        );
        MetaResponse meta = metaMapper.buildMetaResponse(page);
        List<TransactionResponse> transactions = page.getContent();

        return ResponseHelper.ok(transactions, meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(@PathVariable String id) {
        return ResponseHelper.ok(transactionService.getTransaction(id));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<TransactionSummaryResponse>> getSummary(@RequestParam String accountId) {
        return ResponseHelper.ok(transactionService.getSummary(accountId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        transactionService.delete(id);
        return ResponseHelper.ok(null, "Transaction has been deleted");
    }

}
