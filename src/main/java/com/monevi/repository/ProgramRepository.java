package com.monevi.repository;

import java.util.Optional;

import com.monevi.entity.OrganizationRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.Program;

public interface ProgramRepository extends JpaRepository<Program, String>, ProgramCustomRepository {

  Optional<Program> findByIdAndOrganizationRegionAndMarkForDeleteFalse(String programId,
      OrganizationRegion organizationRegion);

  Optional<Program> findByIdAndMarkForDeleteFalse(String programId);
}
