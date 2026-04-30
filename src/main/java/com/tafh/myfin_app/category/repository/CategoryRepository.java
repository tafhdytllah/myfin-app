package com.tafh.myfin_app.category.repository;

import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.projection.CategoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    Optional<CategoryEntity> findByIdAndUser_Id(String id, String userId);

    @Query("""
                SELECT
                    c.id AS id,
                    c.name AS name,
                    c.type AS type,
                    c.active AS active,
                    COUNT(t.id) AS usageCount
                FROM CategoryEntity c
                LEFT JOIN TransactionEntity t
                    ON t.category.id = c.id
                    AND t.account.user.id = :userId
                WHERE c.id = :id
                  AND c.user.id = :userId
                GROUP BY c.id, c.name, c.type, c.active
            """)
    Optional<CategoryProjection> findDetail(
            @Param("id") String id,
            @Param("userId") String userId
    );

    @Query("""
                SELECT
                   c.id AS id,
                   c.name AS name,
                   c.type AS type,
                   c.active AS active,
                   COUNT(t.id) AS usageCount
                FROM CategoryEntity c
                LEFT JOIN TransactionEntity t
                            ON t.category.id = c.id
                            AND t.account.user.id = :userId
                WHERE c.user.id = :userId
                  AND (
                        :type IS NULL OR c.type = :type
                      )
                  AND (
                        :searchTerm IS NULL
                        OR :searchTerm = ''
                        OR LOWER(c.name) LIKE :searchTerm ESCAPE '\\'
                      )
                GROUP BY c.id, c.name, c.type, c.active
                ORDER BY c.name ASC
            """)
    Page<CategoryProjection> findAllWithFilter(
            @Param("userId") String userId,
            @Param("type") CategoryType type,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

}
