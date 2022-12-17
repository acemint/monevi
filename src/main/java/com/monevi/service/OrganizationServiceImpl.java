package com.monevi.service;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Region;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetOrganizationFilter;
import com.monevi.repository.OrganizationRepository;
import com.monevi.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Override
  @Transactional(rollbackFor = ApplicationException.class)
  public Organization create(Organization newOrganizationData, Set<String> regionNames) throws ApplicationException {
    if (this.organizationRepository.findByNameAndMarkForDeleteIsFalse(newOrganizationData.getName()).isPresent()) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_HAS_EXISTED);
    }
    Set<Region> regions = new HashSet<>();
    for (String regionName : regionNames) {
      Region region = this.regionRepository.findByNameAndMarkForDeleteIsFalse(regionName)
          .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.REGION_DOES_NOT_EXIST));
      regions.add(region);
    }
    Set<OrganizationRegion> organizationRegions = this.buildOrganizationRegions(newOrganizationData, regions);
    newOrganizationData.setOrganizationRegions(organizationRegions);
    return this.organizationRepository.save(newOrganizationData);
  }

  @Override
  @Transactional(rollbackFor = ApplicationException.class)
  public Organization updateRegion(Organization existingOrganizationData, Set<String> newRegionNames) throws ApplicationException {
    Organization organization = this.organizationRepository
        .findByNameAndMarkForDeleteIsFalse(existingOrganizationData.getName())
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_DOES_NOT_EXIST));
      Set<String> existingOrganizationRegionNames = organization.getOrganizationRegions()
        .stream()
        .map(r -> r.getRegion().getName())
        .collect(Collectors.toSet());
    for (String newRegionName : newRegionNames) {
      if (existingOrganizationRegionNames.contains(newRegionName)) {
        throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.REGION_FOR_THIS_ORGANIZATION_HAS_EXISTED);
      }
    }
    Set<Region> regions = new HashSet<>();
    for (String newRegionName : newRegionNames) {
      Region region = this.regionRepository.findByNameAndMarkForDeleteIsFalse(newRegionName)
          .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REGION_DOES_NOT_EXIST));
      regions.add(region);
    }
    Set<OrganizationRegion> newOrganizationRegions = this.buildOrganizationRegions(organization, regions);
    organization.getOrganizationRegions().addAll(newOrganizationRegions);
    return this.organizationRepository.save(organization);
  }

  @Override
  public List<Organization> getOrganizations(GetOrganizationFilter filter) {
    return this.organizationRepository.getOrganization(filter);
  }

  private Set<OrganizationRegion> buildOrganizationRegions(Organization organization, Set<Region> regions) {
    Set<OrganizationRegion> organizationRegions = new HashSet<>();
    for (Region region : regions) {
      OrganizationRegion organizationRegion = OrganizationRegion.builder()
          .region(region)
          .organization(organization)
          .build();
      organizationRegions.add(organizationRegion);
    }
    return organizationRegions;
  }

}
