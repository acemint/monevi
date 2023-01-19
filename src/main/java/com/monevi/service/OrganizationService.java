package com.monevi.service;

import com.monevi.entity.Organization;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetOrganizationFilter;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface OrganizationService {

  Organization create(Organization newOrganizationData, Set<String> regionNames) throws ApplicationException;

  Organization updateRegion(Organization existingOrganizationData, Set<String> newRegionNames) throws ApplicationException;

  Page<Organization> getOrganizations(GetOrganizationFilter filter);

}
