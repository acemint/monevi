package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.ProgramToProgramResponseConverter;
import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.dto.request.UpdateProgramRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.ProgramResponse;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetProgramFilter;
import com.monevi.service.ProgramService;
import com.monevi.util.ProgramUrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.PROGRAM)
@Validated
public class ProgramController {

  @Autowired
  private ProgramService programService;

  @Autowired
  @Qualifier(ProgramToProgramResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<Program, ProgramResponse> programToProgramResponseConverter;

  @GetMapping(value = ApiPath.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<ProgramResponse> getPrograms(
      @RequestParam String organizationRegionId,
      @RequestParam(required = false) Integer periodYear,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "name", required = false) String[] sortBy,
      @RequestParam(defaultValue = "true", required = false) String[] isAscending) throws ApplicationException {
    GetProgramFilter filter = this.buildDefaultGetProgramFilter(organizationRegionId, periodYear,
        sortBy, isAscending, page, size);
    Page<Program> responses = this.programService.getPrograms(filter);
    List<ProgramResponse> programResponses = responses.stream()
        .map(p -> this.programToProgramResponseConverter.convert(p)).collect(Collectors.toList());
    return MultipleBaseResponse.<ProgramResponse>builder()
        .values(programResponses)
        .metadata(MultipleBaseResponse.Metadata
            .builder()
            .size(programResponses.size())
            .totalPage(responses.getTotalPages())
            .totalItems(responses.getTotalElements())
            .build())
        .build();
  }

  @PostMapping(value = ApiPath.CREATE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<ProgramResponse> create(
      @Valid @RequestBody CreateProgramRequest createProgramRequest) throws ApplicationException {
    Program program = this.programService.create(createProgramRequest);
    return BaseResponse.<ProgramResponse>builder()
        .value(this.programToProgramResponseConverter.convert(program))
        .build();
  }

  private GetProgramFilter buildDefaultGetProgramFilter(String organizationRegionId,
      Integer periodYear, String[] sortBy, String[] isAscending, int page, int size)
      throws ApplicationException {
    List<Sort.Order> sortOrders = new ArrayList<>();
    int validSize = Math.min(sortBy.length, isAscending.length);
    for (int i = 0; i < validSize; i++) {
      ProgramUrlUtils.checkValidSortedBy(sortBy[i]);
      sortOrders.add(new Sort.Order(ProgramUrlUtils.getSortDirection(isAscending[i]), sortBy[i]));
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
    return GetProgramFilter.builder()
        .organizationRegionId(organizationRegionId)
        .periodYear(periodYear)
        .pageable(pageable)
        .build();
  }

  @PostMapping(value = ApiPath.EDIT, consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<ProgramResponse> updateProgram(@NotBlank @RequestParam String userId,
      @NotBlank @RequestParam String programId,
      @Valid @RequestBody UpdateProgramRequest updateProgramRequest)
      throws ApplicationException {
    Program program = this.programService.updateProgram(userId, programId, updateProgramRequest);
    return BaseResponse.<ProgramResponse>builder()
        .value(this.programToProgramResponseConverter.convert(program)).build();
  }

  @GetMapping(value = ApiPath.LOCK, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<ProgramResponse> lockProgram(@NotBlank @RequestParam String userId,
      @NotBlank @RequestParam String programId) throws ApplicationException {
    Program program = this.programService.lockProgram(userId, programId);
    return BaseResponse.<ProgramResponse>builder()
        .value(this.programToProgramResponseConverter.convert(program)).build();
  }

  @DeleteMapping(value = ApiPath.DELETE)
  public BaseResponse<Boolean> deleteProgram(@RequestParam @NotBlank String programId)
      throws ApplicationException {
    Boolean response = this.programService.deleteProgram(programId);
    return BaseResponse.<Boolean>builder().value(response).build();
  }
}
