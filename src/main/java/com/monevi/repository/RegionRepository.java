package com.monevi.repository;

import com.monevi.entity.Region;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, String> {

  Optional<Region> findByNameAndMarkForDeleteIsFalse(String name);
  Optional<List<Region>> findByMarkForDeleteIsFalse(Sort sort);
  Optional<Region> findByIdAndMarkForDeleteFalse(String regionId);

}
