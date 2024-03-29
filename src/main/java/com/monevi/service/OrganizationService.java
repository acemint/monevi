package com.monevi.service;

import com.monevi.dto.response.OrganizationRegionWithProgramResponse;
import com.monevi.dto.response.OrganizationRegionWithReportResponse;
import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetOrganizationFilter;
import com.monevi.model.GetOrganizationWithProgramExistsFilter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

  Organization create(Organization newOrganizationData, Set<String> regionNames) throws ApplicationException;

  Organization updateRegion(Organization existingOrganizationData, Set<String> newRegionNames) throws ApplicationException;

  Page<Organization> getOrganizations(GetOrganizationFilter filter);

  List<OrganizationRegionWithProgramResponse> getOrganizationsWithProgramExists(String regionId);

  List<OrganizationRegionWithReportResponse> getOrganizationsWithReportExists(String regionId);
}
