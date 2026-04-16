package com.tafh.myfin_app.export.repository;

import com.tafh.myfin_app.export.model.ExportJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExportJobRepository extends JpaRepository<ExportJobEntity, String> {

    Optional<ExportJobEntity> findByIdAndUserId(String id, String userId);
}
