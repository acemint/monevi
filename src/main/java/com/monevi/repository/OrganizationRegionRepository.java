package com.monevi.repository;

import com.monevi.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizationRegionRepository extends JpaRepository<Organization, String> {

  Optional<Organization> findByNameAndMarkForDeleteIsFalse(String name);

}