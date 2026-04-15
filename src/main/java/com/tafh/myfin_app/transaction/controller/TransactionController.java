package com.tafh.myfin_app.transaction.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.MetaMapper;
import com.tafh.myfin_app.common.dto.MetaResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.transaction.dto.TransactionRequest;
import com.tafh.myfin_app.transaction.dto.TransactionResponse;
import com.tafh.myfin_app.transaction.dto.TransactionSummaryResponse;
import com.tafh.myfin_app.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final MetaMapper metaMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> create(@RequestBody TransactionRequest request) {
        return ResponseHelper.created(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAll(
            @RequestParam String accountId,
            Pageable pageable
    ) {
        Page<TransactionResponse> page = transactionService.getAll(accountId, pageable);
        MetaResponse meta = metaMapper.buildMetaResponse(page);

        return ResponseHelper.ok(page.getContent(), meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(@PathVariable String id) {
        return ResponseHelper.ok(transactionService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        transactionService.delete(id);
        return ResponseHelper.ok(null, "Transaction has been deleted");
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<TransactionSummaryResponse>> getSummary(@RequestParam String accountId) {
        return ResponseHelper.ok(transactionService.getSummary(accountId));
    }
}
