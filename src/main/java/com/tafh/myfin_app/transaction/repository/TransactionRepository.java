package com.tafh.myfin_app.transaction.repository;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.projection.IncomeExpenseSummaryProjection;
import com.tafh.myfin_app.transaction.projection.MonthlySummaryProjection;
import com.tafh.myfin_app.transaction.projection.MonthlyTrendProjection;
import com.tafh.myfin_app.transaction.projection.SpendingByCategoryProjection;
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

    @Query("""
                    SELECT t
                    FROM TransactionEntity t
                    JOIN FETCH t.category
                    JOIN FETCH t.account a
                    JOIN FETCH a.user
                    WHERE a.user.id = :userId
            """)
    List<TransactionEntity> findAllByUserIdForExport(
            @Param("userId") String userId
    );

    @Query("""
                SELECT t FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                  AND (:accountId IS NULL OR t.account.id = :accountId)
                  AND (:type IS NULL OR t.type = :type)
                  AND (:categoryId IS NULL OR t.category.id = :categoryId)
                  AND t.createdAt >= :startDateTime
                  AND t.createdAt <= :endDateTime
                  AND (
                        :searchTerm IS NULL
                        OR LOWER(t.description) LIKE :searchTerm ESCAPE '\\'
                        OR LOWER(t.category.name) LIKE :searchTerm ESCAPE '\\'
                      )
            """)
    Page<TransactionEntity> findAllWithFilter(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("type") CategoryType type,
            @Param("categoryId") String categoryId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

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
                    AND t.createdAt BETWEEN :startDateTime AND :endDateTime
                GROUP BY FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM')
                ORDER BY FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM')
            """)
    List<MonthlySummaryProjection> monthlySummary(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
                SELECT
                    t.category.id as categoryId,
                    t.category.name as categoryName,
                    t.category.type as type,
                    COALESCE(SUM(t.amount), 0) as total
                FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                  AND (:accountId IS NULL OR t.account.id = :accountId)
                  AND t.createdAt >= :startDateTime
                  AND t.createdAt <= :endDateTime
                GROUP BY t.category.id, t.category.name, t.category.type
                ORDER BY SUM(t.amount) DESC
            """)
    List<SpendingByCategoryProjection> getSpendingByCategory(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
                SELECT
                    COALESCE(SUM(CASE WHEN t.category.type = 'INCOME' THEN t.amount ELSE 0 END), 0) as income,
                    COALESCE(SUM(CASE WHEN t.category.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) as expense
                FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                  AND (:accountId IS NULL OR t.account.id = :accountId)
                  AND t.createdAt >= :startDateTime
                  AND t.createdAt <= :endDateTime
            """)
    IncomeExpenseSummaryProjection getIncomeExpenseSummary(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
                SELECT
                    FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM') as month,
                    COALESCE(SUM(CASE WHEN t.category.type = 'INCOME' THEN t.amount ELSE 0 END), 0) as income,
                    COALESCE(SUM(CASE WHEN t.category.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) as expense
                FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                  AND (:accountId IS NULL OR t.account.id = :accountId)
                  AND t.createdAt >= :startDateTime
                  AND t.createdAt <= :endDateTime
                GROUP BY FUNCTION('TO_CHAR', t.createdAt, 'YYYY-MM'),
                            YEAR(t.createdAt),
                            MONTH(t.createdAt)
                ORDER BY YEAR(t.createdAt),
                         MONTH(t.createdAt)
            """)
    List<MonthlyTrendProjection> getMonthlyTrend(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
                SELECT t
                FROM TransactionEntity t
                WHERE t.account.user.id = :userId
                  AND (:accountId IS NULL OR t.account.id = :accountId)
                  AND t.createdAt >= :startDateTime
                  AND t.createdAt <= :endDateTime
                ORDER BY t.amount DESC
            """)
    Page<TransactionEntity> findBiggestTransaction(
            @Param("userId") String userId,
            @Param("accountId") String accountId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            Pageable pageable
    );

}
