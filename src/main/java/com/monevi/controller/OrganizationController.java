package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.dto.request.OrganizationAddRegionRequest;
import com.monevi.dto.request.OrganizationCreateNewRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.OrganizationRegionWithProgramResponse;
import com.monevi.dto.response.OrganizationRegionWithReportResponse;
import com.monevi.dto.response.OrganizationResponse;
import com.monevi.entity.Organization;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetOrganizationFilter;
import com.monevi.model.GetOrganizationWithProgramExistsFilter;
import com.monevi.service.OrganizationService;
import com.monevi.util.OrganizationUrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.ORGANIZATION)
public class OrganizationController {

  @Autowired
  private OrganizationService organizationService;

  @Autowired
  private Converter<Organization, OrganizationResponse> organizationToOrganizationResponseConverter;
  
  @Autowired
  private Converter<Organization, OrganizationRegionWithProgramResponse> organizationOrganizationRegionWithProgramResponseConverter;

  @PreAuthorize("hasRole('SUPERVISOR')")
  @PostMapping(path = ApiPath.CREATE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<OrganizationResponse> publishNewOrganizationData(
      @Valid @RequestBody OrganizationCreateNewRequest organizationRequest) throws ApplicationException {
    Organization organizationData = Organization.builder()
        .name(organizationRequest.getName())
        .abbreviation(organizationRequest.getAbbreviation())
        .build();
    Organization organization = this.organizationService.create(organizationData, organizationRequest.getRegionNames());
    return BaseResponse.<OrganizationResponse>builder()
        .value(this.organizationToOrganizationResponseConverter.convert(organization))
        .build();
  }

  @PostMapping(path = ApiPath.ADD_REGION, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<OrganizationResponse> addNewRegion(
      @Valid @RequestBody OrganizationAddRegionRequest organizationRequest) throws ApplicationException {
    Organization organizationData = Organization.builder()
        .name(organizationRequest.getName())
        .build();
    Organization organization = this.organizationService.updateRegion(organizationData, organizationRequest.getRegionNames());
    return BaseResponse.<OrganizationResponse>builder()
        .value(this.organizationToOrganizationResponseConverter.convert(organization))
        .build();
  }

  @GetMapping(path = ApiPath.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<OrganizationResponse> getOrganizations(
      @RequestParam(required = false) String searchTerm,
      @RequestParam(required = false) String regionName,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "name", required = false) String[] sortBy,
      @RequestParam(defaultValue = "true", required = false) String[] isAscending) throws ApplicationException {
    GetOrganizationFilter filter = this.buildDefaultGetOrganizationFilter(searchTerm, regionName, sortBy, isAscending, page, size);
    Page<Organization> responses = this.organizationService.getOrganizations(filter);
    List<OrganizationResponse> organizationResponses = responses.getContent().stream()
        .map(o -> this.organizationToOrganizationResponseConverter.convert(o))
        .collect(Collectors.toList());
    return MultipleBaseResponse.<OrganizationResponse>builder()
        .values(organizationResponses)
        .metadata(MultipleBaseResponse.Metadata
            .builder()
            .size(organizationResponses.size())
            .totalPage(responses.getTotalPages())
            .totalItems(responses.getTotalElements())
            .build())
        .build();
  }

  private GetOrganizationFilter buildDefaultGetOrganizationFilter(
      String searchTerm, String regionName, String[] sortBy, String[] isAscending, int page, int size)
      throws ApplicationException {
    List<Sort.Order> sortOrders = new ArrayList<>();
    int validSize = Math.min(sortBy.length, isAscending.length);
    for (int i = 0; i < validSize; i++) {
      OrganizationUrlUtils.checkValidSortedBy(sortBy[i]);
      sortOrders.add(new Sort.Order(OrganizationUrlUtils.getSortDirection(isAscending[i]), sortBy[i]));
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
    return GetOrganizationFilter.builder()
        .searchTerm(searchTerm)
        .regionName(regionName)
        .pageable(pageable)
        .build();
  }

  @GetMapping(path = ApiPath.FIND_ORGANIZATION_WITH_PROGRAM,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<OrganizationRegionWithProgramResponse> getOrganizationsWithProgramExists(
      @RequestParam String regionId,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "name", required = false) String[] sortBy,
      @RequestParam(defaultValue = "true", required = false) String[] isAscending)
      throws ApplicationException {
    List<OrganizationRegionWithProgramResponse> responses =
        this.organizationService.getOrganizationsWithProgramExists(regionId);
    return MultipleBaseResponse.<OrganizationRegionWithProgramResponse>builder()
        .values(responses)
        .metadata(MultipleBaseResponse.Metadata.builder()
            .size(responses.size())
            .totalPage(0)
            .totalItems(responses.size())
            .build())
        .build();
  }

  @GetMapping(path = ApiPath.FIND_ORGANIZATION_WITH_REPORT, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<OrganizationRegionWithReportResponse> getOrganizationsWithReportExists(
      @RequestParam String regionId,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "name", required = false) String[] sortBy,
      @RequestParam(defaultValue = "true", required = false) String[] isAscending) throws ApplicationException {
    List<OrganizationRegionWithReportResponse> organizationRegionWithReportResponses = this.organizationService.getOrganizationsWithReportExists(regionId);
    return MultipleBaseResponse.<OrganizationRegionWithReportResponse>builder()
        .values(organizationRegionWithReportResponses)
        .metadata(MultipleBaseResponse.Metadata.builder()
            .size(organizationRegionWithReportResponses.size())
            .totalPage(0)
            .totalItems(organizationRegionWithReportResponses.size())
            .build())
        .build();
  }

//  private GetOrganizationWithProgramExistsFilter buildDefaultGetOrganizationWithProgramFilter(
//      String regionId, Integer periodYear, String[] sortBy, String[] isAscending, int page,
//      int size) throws ApplicationException {
//    List<Sort.Order> sortOrders = new ArrayList<>();
//    int validSize = Math.min(sortBy.length, isAscending.length);
//    for (int i = 0; i < validSize; i++) {
//      OrganizationUrlUtils.checkValidSortedBy(sortBy[i]);
//      sortOrders
//          .add(new Sort.Order(OrganizationUrlUtils.getSortDirection(isAscending[i]), sortBy[i]));
//    }
//    Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
//    return GetOrganizationWithProgramExistsFilter.builder()
//        .regionId(regionId)
//        .periodYear(periodYear)
//        .pageable(pageable).build();
//  }
}
