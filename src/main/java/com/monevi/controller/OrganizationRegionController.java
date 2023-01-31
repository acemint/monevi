package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.OrganizationRegionToOrganizationRegionResponseConverter;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.OrganizationRegionResponse;
import com.monevi.entity.OrganizationRegion;
import com.monevi.exception.ApplicationException;
import com.monevi.service.OrganizationRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.ORGANIZATION_REGION)
@Validated
public class OrganizationRegionController {

  @Autowired
  private OrganizationRegionService organizationRegionService;

  @Autowired
  @Qualifier(value = OrganizationRegionToOrganizationRegionResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<OrganizationRegion, OrganizationRegionResponse> organizationRegionOrganizationRegionResponseConverter;

  @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<OrganizationRegionResponse> getOrganization(
      @RequestParam String id) throws ApplicationException {
    OrganizationRegion organizationRegion = this.organizationRegionService.findOrganizationRegion(id);
    return BaseResponse.<OrganizationRegionResponse>builder()
        .value(this.organizationRegionOrganizationRegionResponseConverter.convert(organizationRegion))
        .build();
  }


}
