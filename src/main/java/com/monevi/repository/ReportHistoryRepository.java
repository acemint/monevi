package com.monevi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.ReportHistory;

public interface ReportHistoryRepository
    extends JpaRepository<ReportHistory, String>, ReportHistoryCustomRepository {
}
