package com.monevi.converter;

import com.monevi.dto.response.OrganizationResponse;
import com.monevi.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component(value = OrganizationToOrganizationReponseConverter.ORGANIZATION_TO_ORGANIZATION_RESPONSE_BEAN_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class OrganizationToOrganizationReponseConverter implements Converter<Organization, OrganizationResponse> {

  public static final String ORGANIZATION_TO_ORGANIZATION_RESPONSE_BEAN_NAME = "OrganizationToOrganizationResponse";

  @Override
  public OrganizationResponse convert(Organization source) {
    return OrganizationResponse.builder()
        .id(source.getId())
        .name(source.getName())
        .abbreviation(source.getAbbreviation())
        .regionNames(source.getOrganizationRegions()
            .stream().map(or -> or.getRegion().getName()).collect(Collectors.toList()))
        .build();
  }

}
