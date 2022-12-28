package com.monevi.repository;

import com.monevi.entity.Report;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, String>, ReportCustomRepository {

}
