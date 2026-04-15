package com.tafh.myfin_app.transaction.repository;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.projection.MonthlySummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                    AND (:accountId IS NULL OR t.account.id = :accountId)
                    AND t.category.type = :type
                    AND t.createdAt BETWEEN :start AND :end
            """)
    BigDecimal sumByTypeAndDateRange(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("type") CategoryType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
                SELECT
                    FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM') as month,
                    SUM(CASE
                        WHEN t.category.type = com.tafh.myfin_app.category.model.CategoryType.INCOME
                        THEN t.amount ELSE 0 END) as totalIncome,
                    SUM(CASE
                        WHEN t.category.type = com.tafh.myfin_app.category.model.CategoryType.EXPENSE
                        THEN t.amount ELSE 0 END) as totalExpense
                FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                    AND (:accountId IS NULL OR t.account.id = :accountId)
                    AND t.createdAt BETWEEN :start AND :end
                GROUP BY FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM')
                ORDER BY FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM')
            """)
    List<MonthlySummaryProjection> monthlySummary(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
