package com.monevi.repository;

import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Report;
import com.monevi.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRegionRepository extends JpaRepository<OrganizationRegion, String> {

  Optional<OrganizationRegion> findByIdAndMarkForDeleteIsFalse(String id);

}
