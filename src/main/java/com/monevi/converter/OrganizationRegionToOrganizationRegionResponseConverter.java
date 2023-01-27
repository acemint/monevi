package com.monevi.converter;

import com.monevi.dto.response.OrganizationRegionResponse;
import com.monevi.entity.OrganizationRegion;
import org.springframework.stereotype.Component;

@Component(value = OrganizationRegionToOrganizationRegionResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
public class OrganizationRegionToOrganizationRegionResponseConverter implements Converter<OrganizationRegion, OrganizationRegionResponse> {

  public static final String COMPONENT_NAME =
      "organizationRegionToOrganizationRegionResponse";

  @Override
  public OrganizationRegionResponse convert(OrganizationRegion source) {
    return OrganizationRegionResponse.builder()
        .id(source.getId())
        .organizationName(source.getOrganization().getName())
        .regionName(source.getRegion().getName())
        .build();
  }
}
