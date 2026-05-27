package com.tafh.myfin_app.account.controller;

import com.tafh.myfin_app.account.dto.AccountRequest;
import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.dto.StatusAccountRequest;
import com.tafh.myfin_app.account.dto.UpdateAccountRequest;
import com.tafh.myfin_app.account.service.AccountService;
import com.tafh.myfin_app.category.dto.CategoryResponse;
import com.tafh.myfin_app.category.dto.StatusCategoryRequest;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.MetaMapper;
import com.tafh.myfin_app.common.dto.MetaResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final MetaMapper metaMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(@Valid @RequestBody AccountRequest request) {
        return ResponseHelper.created(accountService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateAccountRequest request
    ) {
        return ResponseHelper.ok(accountService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AccountResponse>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody StatusAccountRequest request
    ) {
        return ResponseHelper.ok(accountService.updateStatus(id, request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccounts(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<AccountResponse> page = accountService.getAccounts(active, keyword, pageable);
        MetaResponse meta = metaMapper.buildMetaResponse(page);
        List<AccountResponse> accounts = page.getContent();

        return ResponseHelper.ok(accounts, meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String id) {
        return ResponseHelper.ok(accountService.getAccount(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        accountService.delete(id);
        return ResponseHelper.ok(null, "Account has been deleted");
    }
}
