package com.tafh.myfin_app.transaction.repository;

import com.tafh.myfin_app.category.model.CategoryType;
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
                    WHERE t.account.id = :accountId
                       AND t.account.user.id = :userId
                          AND t.type = :type
            """)
    BigDecimal sumByAccountAndUserAndType(
            @Param("accountId") String accountId,
            @Param("userId") String userId,
            @Param("type") CategoryType type
    );


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
                FROM TransactionEntity t
                    WHERE t.account.user.id = :userId
                                AND t.type = :type
            """)
    BigDecimal sumByUserAndType(
            @Param("userId") String userId,
            @Param("type") CategoryType type
    );

}
