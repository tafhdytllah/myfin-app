package com.tafh.myfin_app.account.repository;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.projection.AccountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    Optional<AccountEntity> findByIdAndUser_Id(String id, String userId);

    Boolean existsByUser_IdAndNameIgnoreCase(String userId, String name);

    @Query("""
                SELECT
                    a.id AS id,
                    a.name AS name,
                    a.openingBalance AS openingBalance,
                    a.currentBalance AS currentBalance,
                    a.active AS active,
                    COUNT(a.id) AS usageCount
                FROM AccountEntity a
                WHERE a.id = :id
                  AND a.user.id = :userId
                GROUP BY a.id, a.name, a.openingBalance, a.currentBalance, a.active
            """)
    Optional<AccountProjection> findDetail(
            @Param("id") String id,
            @Param("userId") String userId
    );

    @Query("""
                SELECT
                    a.id AS id,
                    a.name AS name,
                    a.openingBalance AS openingBalance,
                    a.currentBalance AS currentBalance,
                    a.active AS active,
                    COUNT(a.id) AS usageCount
                FROM AccountEntity a
                WHERE a.user.id = :userId
                  AND (
                        :active IS NULL OR a.active = :active
                      )
                  AND (
                        :searchTerm IS NULL
                        OR :searchTerm = ''
                        OR LOWER(a.name) LIKE :searchTerm ESCAPE '\\'
                      )
                  GROUP BY a.id, a.name, a.openingBalance, a.currentBalance, a.active
            """)
    List<AccountProjection> findAllWithFilter(
            @Param("userId") String userId,
            @Param("active") Boolean active,
            @Param("searchTerm") String searchTerm
    );


}
