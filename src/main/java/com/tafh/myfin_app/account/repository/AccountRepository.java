package com.tafh.myfin_app.account.repository;

import com.tafh.myfin_app.account.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    List<AccountEntity> findAllByUserId(String userId);

    Optional<AccountEntity> findByIdAndUserId(String id, String userId);

}
