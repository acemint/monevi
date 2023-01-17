package com.monevi.service;

import com.monevi.entity.Region;
import com.monevi.model.GetRegionFilter;

import java.util.List;

public interface RegionService {

  List<Region> getRegions(GetRegionFilter filter);

}
