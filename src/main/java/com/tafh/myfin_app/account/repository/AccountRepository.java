package com.tafh.myfin_app.account.repository;

import com.tafh.myfin_app.account.model.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    Optional<AccountEntity> findByIdAndUser_Id(String id, String userId);

    @Query("""
                SELECT a
                FROM AccountEntity a
                WHERE a.user.id = :userId
                  AND (
                        :searchTerm IS NULL
                        OR :searchTerm = ''
                        OR LOWER(a.name) LIKE :searchTerm ESCAPE '\\'
                      )
            """)
    Page<AccountEntity> findAllWithFilter(
            @Param("userId") String userId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );


}
