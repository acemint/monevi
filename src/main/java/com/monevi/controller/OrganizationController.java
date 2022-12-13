package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.dto.request.OrganizationCreateNewRequest;
import com.monevi.dto.request.OrganizationUpdateRegionRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.OrganizationResponse;
import com.monevi.entity.Organization;
import com.monevi.exception.ApplicationException;
import com.monevi.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(OrganizationControllerPath.BASE)
public class OrganizationController {

  @Autowired
  private OrganizationService organizationService;

  @Autowired
  private Converter<Organization, OrganizationResponse> organizationToOrganizationResponseConverter;

  @PostMapping(path = OrganizationControllerPath.CREATE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<OrganizationResponse> publishNewOrganizationData(
      @Valid @RequestBody OrganizationCreateNewRequest organizationRequest) throws ApplicationException {
    Organization organizationData = Organization.builder()
        .name(organizationRequest.getName())
        .abbreviation(organizationRequest.getAbbreviation())
        .build();
    Organization organization = organizationService.create(organizationData, organizationRequest.getRegionNames());
    return BaseResponse.<OrganizationResponse>builder()
        .value(organizationToOrganizationResponseConverter.convert(organization))
        .build();
  }

  @PatchMapping(path = OrganizationControllerPath.ADD_REGION, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<OrganizationResponse> updateNewOrganizationData(
      @Valid @RequestBody OrganizationUpdateRegionRequest organizationRequest) throws ApplicationException {
    Organization organizationData = Organization.builder()
        .name(organizationRequest.getName())
        .build();
    Organization organization = organizationService.updateRegion(organizationData, organizationRequest.getRegionNames());
    return BaseResponse.<OrganizationResponse>builder()
        .value(organizationToOrganizationResponseConverter.convert(organization))
        .build();
  }

}
