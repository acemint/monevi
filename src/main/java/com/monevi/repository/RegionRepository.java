package com.monevi.repository;

import com.monevi.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RegionRepository extends JpaRepository<Region, String> {

  Optional<Region> findByNameAndMarkForDeleteIsFalse(String name);
  Optional<Set<Region>> findByMarkForDeleteIsFalse();

}
