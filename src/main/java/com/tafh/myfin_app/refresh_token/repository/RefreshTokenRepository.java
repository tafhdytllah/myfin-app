package com.tafh.myfin_app.refresh_token.repository;


import com.tafh.myfin_app.refresh_token.model.RefreshTokenEntity;
import com.tafh.myfin_app.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {

    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    Optional<RefreshTokenEntity> findByTokenHashAndRevokedIsFalse(String tokenHash);

    Optional<RefreshTokenEntity> findTopByUserOrderByExpiresAtDesc(UserEntity user);

    @Modifying
    @Query("""
        update RefreshTokenEntity t
        set t.revoked = true
        where t.user = :user
            and t.revoked = false
    """)
    void revokeAllByUser(@Param("user") UserEntity user);

    @Modifying
    @Query("""
        delete from RefreshTokenEntity t
        where t.revoked = true
             and t.expiresAt < CURRENT_TIMESTAMP
    """)
    void deleteExpiredAndRevoked();

}
