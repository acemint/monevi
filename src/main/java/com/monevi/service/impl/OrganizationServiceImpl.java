package com.monevi.service.impl;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.response.OrganizationRegionWithProgramResponse;
import com.monevi.dto.response.OrganizationRegionWithReportResponse;
import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Region;
import com.monevi.enums.ReportStatus;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetOrganizationFilter;
import com.monevi.repository.OrganizationRepository;
import com.monevi.repository.RegionRepository;
import com.monevi.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.util.ArrayList;
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
  public Page<Organization> getOrganizations(GetOrganizationFilter filter) {
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

  @Override
  public List<OrganizationRegionWithProgramResponse> getOrganizationsWithProgramExists(String regionId){
    List<OrganizationRegionWithProgramResponse> organizationRegionWithProgramResponses = new ArrayList<>();
    List<Tuple> organizations = this.organizationRepository.getOrganizationsWithProgram(regionId);
    for (Tuple organization : organizations) {
      String organizationRegionId = organization.get("organization_region_id", String.class);
      String name = organization.get("organization_name", String.class);
      String abbreviation = organization.get("organization_abbreviation", String.class);
      Integer periodYear = organization.get("period_year", Integer.class);
      OrganizationRegionWithProgramResponse organizationRegionWithReportResponse = OrganizationRegionWithProgramResponse
          .builder()
          .organizationRegionId(organizationRegionId)
          .organizationName(name)
          .organizationAbbreviation(abbreviation)
          .periodYear(periodYear)
          .build();
      organizationRegionWithProgramResponses.add(organizationRegionWithReportResponse);
    }
    return organizationRegionWithProgramResponses;
  }

  @Override
  public List<OrganizationRegionWithReportResponse> getOrganizationsWithReportExists(String regionId) {
    List<OrganizationRegionWithReportResponse> organizationRegionWithReportResponses = new ArrayList<>();
    List<Tuple> organizations = this.organizationRepository.getOrganizationsWithReport(regionId);
    for (Tuple organization : organizations) {
      String organizationRegionId = organization.get("organization_region_id", String.class);
      String name = organization.get("organization_name", String.class);
      String abbreviation = organization.get("organization_abbreviation", String.class);
      Timestamp periodDate = organization.get("period_date", Timestamp.class);
      ReportStatus reportStatus = ReportStatus.valueOf(organization.get("status", String.class));
      OrganizationRegionWithReportResponse organizationRegionWithReportResponse = OrganizationRegionWithReportResponse
          .builder()
          .organizationRegionId(organizationRegionId)
          .organizationName(name)
          .organizationAbbreviation(abbreviation)
          .periodDate(periodDate)
          .reportStatus(reportStatus)
          .build();
      organizationRegionWithReportResponses.add(organizationRegionWithReportResponse);
    }
    return organizationRegionWithReportResponses;
  }
}
