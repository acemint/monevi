package com.monevi.service;

import com.monevi.entity.OrganizationRegion;
import com.monevi.exception.ApplicationException;

public interface OrganizationRegionService {

  OrganizationRegion findOrganizationRegion(String id) throws ApplicationException;

}
