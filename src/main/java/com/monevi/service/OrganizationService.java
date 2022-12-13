package com.monevi.service;

import com.monevi.entity.Organization;
import com.monevi.exception.ApplicationException;

import java.util.Set;

public interface OrganizationService {

  Organization create(Organization newOrganizationData, Set<String> regionNames) throws ApplicationException;

  Organization updateRegion(Organization existingOrganizationData, Set<String> newRegionNames) throws ApplicationException;

}
