package com.monevi.converter;

import com.monevi.dto.response.ProgramResponse;
import com.monevi.entity.Program;
import org.springframework.stereotype.Component;

@Component(value = ProgramToProgramResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
public class ProgramToProgramResponseConverter implements Converter<Program, ProgramResponse> {

  public static final String COMPONENT_NAME = "ProgramToProgramResponse";

  @Override
  public ProgramResponse convert(Program source) {
    return ProgramResponse.builder()
        .id(source.getId())
        .name(source.getName())
        .budget(source.getBudget())
        .subsidy(source.getSubsidy())
        .startDate(source.getStartDate().getTime())
        .endDate(source.getEndDate().getTime())
        .build();
  }
}
