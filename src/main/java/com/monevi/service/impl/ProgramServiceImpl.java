package com.monevi.service.impl;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetProgramFilter;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.ProgramRepository;
import com.monevi.service.ProgramService;
import com.monevi.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramServiceImpl implements ProgramService {

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private ProgramRepository programRepository;

  @Override
  public Program create(CreateProgramRequest request) throws ApplicationException {
    OrganizationRegion organizationRegion = this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(request.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    Program program = Program.builder()
        .name(request.getProgramName())
        .budget(request.getBudget())
        .subsidy(request.getSubsidy())
        .startDate(DateUtils.dateInputToTimestamp(request.getStartDate()))
        .endDate(DateUtils.dateInputToTimestamp(request.getEndDate()))
        .organizationRegion(organizationRegion)
        .build();
    program.setOrganizationRegion(organizationRegion);
    return this.programRepository.save(program);
  }

  @Override
  public List<Program> getPrograms(GetProgramFilter filter) throws ApplicationException {
    return this.programRepository.getPrograms(filter)
        .orElse(Collections.emptyList());
  }

}
