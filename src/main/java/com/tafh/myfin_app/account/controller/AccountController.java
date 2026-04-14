package com.tafh.myfin_app.account.controller;

import com.tafh.myfin_app.account.dto.AccountRequest;
import com.tafh.myfin_app.account.dto.AccountResponse;
import com.tafh.myfin_app.account.service.AccountService;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(@RequestBody AccountRequest request) {
        return ResponseHelper.created(accountService.create(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> findAll() {
        return ResponseHelper.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> findById(@PathVariable String id) {
        return ResponseHelper.ok(accountService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable String id,
            @RequestBody AccountRequest request
    ) {
        return ResponseHelper.created(accountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> delete(@PathVariable String id) {
        accountService.delete(id);

        return ResponseHelper.ok(null, "Account deleted");
    }
}
