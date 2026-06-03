package com.tafh.myfin_app.transaction.model;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.common.exception.DomainException;
import com.tafh.myfin_app.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
public class TransactionEntity extends BaseEntity {

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private CategoryType type;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public static TransactionEntity create(
            AccountEntity account,
            CategoryEntity category,
            BigDecimal amount,
            CategoryType type,
            String description
    ) {
        validateTransactionData(account, category, amount, type);

        TransactionEntity transaction = new TransactionEntity();
        transaction.account = account;
        transaction.category = category;
        transaction.amount = amount;
        transaction.type = type;
        transaction.description = description;

        return transaction;
    }

    public void update(
            AccountEntity account,
            CategoryEntity category,
            BigDecimal amount,
            CategoryType type,
            String description
    ) {
        validateTransactionData(account, category, amount, type);

        this.account = account;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    public void applyEffect() {
        if (isIncome()) {
            increaseBalance();
            return;
        }

        decreaseBalance();
    }

    public void revertEffect() {
        if (isIncome()) {
            decreaseBalance();
            return;
        }

        increaseBalance();
    }

    private static void validateTransactionData(AccountEntity account, CategoryEntity category, BigDecimal amount, CategoryType type) {
        if (account == null) throw new DomainException("Account is required");
        if (category == null) throw new DomainException("Category is required");
        if (amount == null) throw new DomainException("Amount is required");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new DomainException("Amount must be greater than zero");
        if (type == null) throw new DomainException("Transaction type is required");
        if (category.getType() != type) throw new DomainException("Category type mismatch");
    }

    private boolean isIncome() {
        return type == CategoryType.INCOME;
    }

    private void increaseBalance() {
        account.increaseBalance(amount);
    }

    private void decreaseBalance() {
        account.decreaseBalance(amount);
    }

}
