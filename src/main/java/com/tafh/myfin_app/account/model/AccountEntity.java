package com.tafh.myfin_app.account.model;

import com.tafh.myfin_app.common.model.BaseEntity;
import com.tafh.myfin_app.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class AccountEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static AccountEntity create(UserEntity user, String name, BigDecimal balance) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Account name required");
        }

        AccountEntity account = new AccountEntity();
        account.user = user;
        account.name = name;
        account.balance = balance == null ? BigDecimal.ZERO : balance;

        return account;
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Account name required");
        }
        this.name = name;
    }

    public void increaseBalance(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        validateAmount(amount);

        if (this.balance.compareTo(amount) <= 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        this.balance = this.balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

}

























