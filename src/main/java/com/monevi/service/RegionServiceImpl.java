package com.monevi.service;

import com.monevi.entity.Region;
import com.monevi.model.GetRegionFilter;
import com.monevi.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class RegionServiceImpl implements RegionService {

  @Autowired
  private RegionRepository regionRepository;

  @Override
  public List<Region> getRegions(GetRegionFilter filter) {
    return this.regionRepository.findByMarkForDeleteIsFalse(filter.getSort()).orElse(Collections.emptyList());
  }

}
