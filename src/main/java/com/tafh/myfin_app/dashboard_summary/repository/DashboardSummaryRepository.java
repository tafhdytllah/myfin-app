package com.tafh.myfin_app.dashboard_summary.repository;

import com.tafh.myfin_app.dashboard_summary.model.DashboardSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DashboardSummaryRepository extends JpaRepository<DashboardSummaryEntity, String> {

    Optional<DashboardSummaryEntity> findByUserIdAndAccountIdAndPeriod(String userId, String accountId, String period);

    List<DashboardSummaryEntity> findByUserIdAndPeriod(String userId, String period);

}
