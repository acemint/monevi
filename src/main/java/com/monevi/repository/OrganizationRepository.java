package com.monevi.repository;

import com.monevi.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, String>, OrganizationCustomRepository {

  Optional<Organization> findByNameAndMarkForDeleteIsFalse(String name);

}
