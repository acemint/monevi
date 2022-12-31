package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.ProgramToProgramResponseConverter;
import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.ProgramResponse;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;
import com.monevi.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.PROGRAM)
public class ProgramController {

  @Autowired
  private ProgramService programService;

  @Autowired
  @Qualifier(ProgramToProgramResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<Program, ProgramResponse> programToProgramResponseConverter;

  @PostMapping(value = ApiPath.CREATE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<ProgramResponse> create(
      @Valid @RequestBody CreateProgramRequest createProgramRequest) throws ApplicationException {
    Program program = this.programService.create(createProgramRequest);
    return BaseResponse.<ProgramResponse>builder()
        .value(this.programToProgramResponseConverter.convert(program))
        .build();
  }

}
