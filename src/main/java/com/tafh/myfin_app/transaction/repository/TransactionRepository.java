package com.tafh.myfin_app.transaction.repository;

import com.tafh.myfin_app.transaction.enums.TransactionType;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    Page<TransactionEntity> findByAccountId(String accountId, Pageable pageable);

    Optional<TransactionEntity> findByIdAndAccountUserId(String id, String userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
                FROM TransactionEntity t
                    WHERE t.account.user.id = :userId
                       AND t.account.id = :accountId
                          AND t.type = :type
            """)
    BigDecimal sumByAccountAndUserAndType(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("type") TransactionType type
    );

}
