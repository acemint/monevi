package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.RegionResponse;
import com.monevi.entity.Region;
import com.monevi.model.GetRegionFilter;
import com.monevi.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.REGION)
@Validated
public class RegionController {

  @Autowired
  private RegionService regionService;

  @Autowired
  private Converter<Region, RegionResponse> regionToRegionResponseConverter;

  @GetMapping(path = ApiPath.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<RegionResponse> getAvailableRegions() {
    GetRegionFilter getRegionFilter = this.buildDefaultGetRegionFilter();
    List<RegionResponse> regionResponses = this.regionService.getRegions(getRegionFilter)
        .stream()
        .map(r -> this.regionToRegionResponseConverter.convert(r))
        .collect(Collectors.toList());
    return MultipleBaseResponse.<RegionResponse>builder()
        .values(regionResponses)
        .metadata(MultipleBaseResponse.Metadata
            .builder()
            .size(regionResponses.size())
            .totalPage(0)
            .totalItems(regionResponses.size())
            .build())
        .build();
  }

  private GetRegionFilter buildDefaultGetRegionFilter() {
    List<Sort.Order> sortOrders = new ArrayList<>();
    sortOrders.add(new Sort.Order(Sort.Direction.ASC, "name"));
    return GetRegionFilter.builder()
        .sort(Sort.by(sortOrders))
        .build();
  }

}
