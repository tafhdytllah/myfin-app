package com.tafh.myfin_app.transaction.model;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.common.model.BaseEntity;
import com.tafh.myfin_app.transaction.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
public class TransactionEntity extends BaseEntity {

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10, nullable = false)
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    public static TransactionEntity create(AccountEntity account, BigDecimal amount, TransactionType type, String description) {
        TransactionEntity trx = new TransactionEntity();
        trx.account = account;
        trx.amount = amount;
        trx.type = type;
        trx.description = description;

        if (type == TransactionType.INCOME) {
            account.increaseBalance(amount);
        } else {
            account.decreaseBalance(amount);
        }

        return trx;
    }

}
