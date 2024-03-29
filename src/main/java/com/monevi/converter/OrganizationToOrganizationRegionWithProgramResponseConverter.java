package com.monevi.converter;

import org.springframework.stereotype.Component;

import com.monevi.dto.response.OrganizationRegionWithProgramResponse;
import com.monevi.entity.Organization;

@Deprecated
@Component(
    value = OrganizationToOrganizationRegionWithProgramResponseConverter.ORGANIZATION_TO_ORGANIZATION_REGION_WITH_PROGRAM_RESPONSE_BEAN_NAME
        + Converter.SUFFIX_BEAN_NAME)
public class OrganizationToOrganizationRegionWithProgramResponseConverter
    implements Converter<Organization, OrganizationRegionWithProgramResponse> {

  public static final String ORGANIZATION_TO_ORGANIZATION_REGION_WITH_PROGRAM_RESPONSE_BEAN_NAME =
      "OrganizationToOrganizationRegionWithProgramResponse";

  @Override
  public OrganizationRegionWithProgramResponse convert(Organization source) {
    return OrganizationRegionWithProgramResponse.builder()
        .organizationRegionId(source.getId())
        .organizationAbbreviation(source.getAbbreviation())
        .organizationName(source.getName())
        .build();
  }
}
