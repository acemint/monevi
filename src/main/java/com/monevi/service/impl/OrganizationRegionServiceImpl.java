package com.monevi.service.impl;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.OrganizationRegion;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.service.OrganizationRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrganizationRegionServiceImpl implements OrganizationRegionService {

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Override
  public OrganizationRegion findOrganizationRegion(String id) throws ApplicationException {
     return this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(id)
         .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
  }
}
