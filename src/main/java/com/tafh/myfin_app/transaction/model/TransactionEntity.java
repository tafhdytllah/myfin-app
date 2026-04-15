package com.tafh.myfin_app.transaction.model;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
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

    public static TransactionEntity create(AccountEntity account, CategoryEntity category,BigDecimal amount, CategoryType type, String description) {
        if (category.getType() != type) {
            throw new RuntimeException("Category type mismatch");
        }

        TransactionEntity trx = new TransactionEntity();
        trx.account = account;
        trx.category = category;
        trx.amount = amount;
        trx.type = type;
        trx.description = description;

        if (type == CategoryType.INCOME) {
            account.increaseBalance(amount);
        } else {
            account.decreaseBalance(amount);
        }

        return trx;
    }

}
