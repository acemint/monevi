package com.monevi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.Report;
import com.monevi.entity.ReportComment;

public interface ReportCommentRepository extends JpaRepository<ReportComment, String> {

  Optional<ReportComment> findByReportAndMarkForDeleteFalse(Report report);
}
