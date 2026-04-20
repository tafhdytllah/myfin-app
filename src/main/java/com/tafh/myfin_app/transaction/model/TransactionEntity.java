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
        if (account == null) throw new DomainException("Account is required");
        if (category == null) throw new DomainException("Category is required");
        if (amount == null) throw new DomainException("Amount is required");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new DomainException("Amount must be greater than zero");
        if (type == null) throw new DomainException("Transaction type is required");
        if (category.getType() != type) throw new DomainException("Category type mismatch");

        TransactionEntity transaction = new TransactionEntity();
        transaction.account = account;
        transaction.category = category;
        transaction.amount = amount;
        transaction.type = type;
        transaction.description = description;

        return transaction;
    }

}
