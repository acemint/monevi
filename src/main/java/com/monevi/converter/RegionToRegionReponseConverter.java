package com.monevi.converter;

import com.monevi.dto.response.OrganizationResponse;
import com.monevi.dto.response.RegionResponse;
import com.monevi.entity.Organization;
import com.monevi.entity.Region;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component(value = RegionToRegionReponseConverter.REGION_TO_REGION_RESPONSE_BEAN_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class RegionToRegionReponseConverter implements Converter<Region, RegionResponse> {

  public static final String REGION_TO_REGION_RESPONSE_BEAN_NAME = "RegionToRegionResponse";

  @Override
  public RegionResponse convert(Region source) {
    return RegionResponse.builder()
        .id(source.getId())
        .name(source.getName())
        .build();
  }
}
