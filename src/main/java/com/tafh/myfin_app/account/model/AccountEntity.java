package com.tafh.myfin_app.account.model;

import com.tafh.myfin_app.common.exception.DomainException;
import com.tafh.myfin_app.common.model.BaseEntity;
import com.tafh.myfin_app.common.util.LogHelper;
import com.tafh.myfin_app.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class AccountEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "opening_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal openingBalance;

    @Column(name = "current_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentBalance;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static AccountEntity create(UserEntity user, String name, BigDecimal openingBalance) {

        if (user == null) {
            throw new DomainException("User is required");
        }

        if (name == null || name.isBlank()) {
            throw new DomainException("Account name is required");
        }

        if (openingBalance != null && openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Balance cannot be negative");
        }

        BigDecimal initialBalance = openingBalance == null ? BigDecimal.ZERO : openingBalance;

        AccountEntity account = new AccountEntity();
        account.user = user;
        account.name = name;
        account.openingBalance = initialBalance;
        account.currentBalance = initialBalance;
        account.active = true;

        return account;
    }

    public void update(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Account name is required");
        }

        this.name = name;
    }

    public void active() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void increaseBalance(BigDecimal amount) {
        validateAmount(amount);

        this.currentBalance = this.currentBalance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        validateAmount(amount);

        if (this.currentBalance.compareTo(amount) < 0) {
            throw new DomainException("Insufficient balance");
        }

        this.currentBalance = this.currentBalance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new DomainException("Amount is required");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Amount must be greater than zero");
        }
    }

}