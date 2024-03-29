package com.monevi.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.dto.request.UpdateProgramRequest;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Program;
import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetProgramFilter;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.ProgramRepository;
import com.monevi.repository.UserAccountRepository;
import com.monevi.service.ProgramService;
import com.monevi.util.DateUtils;

@Service
public class ProgramServiceImpl implements ProgramService {

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private ProgramRepository programRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;
  
  @Override
  public Program create(CreateProgramRequest request) throws ApplicationException {
    UserAccount user =
        this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(request.getUserId())
            .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    OrganizationRegion organizationRegion = this.organizationRegionRepository
        .findByIdAndMarkForDeleteIsFalse(request.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));

    if (!organizationRegion.equals(user.getOrganizationRegion())) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.USER_AND_ORGANIZATION_REGION_NOT_MATCH);
    }

    this.validateDate(request.getStartDate());
    this.validateDate(request.getEndDate());
    if (this.validateStartDateAndEndDate(request.getStartDate(), request.getEndDate())) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.INVALID_START_DATE_END_DATE);
    }
    
    Program program = Program.builder()
        .name(request.getProgramName())
        .budget(request.getBudget())
        .subsidy(request.getSubsidy())
        .startDate(DateUtils.dateInputToTimestamp(request.getStartDate()))
        .endDate(DateUtils.dateInputToTimestamp(request.getEndDate()))
        .organizationRegion(organizationRegion)
        .periodYear(user.getPeriodYear())
        .build();
    program.setOrganizationRegion(organizationRegion);
    return this.programRepository.save(program);
  }

  private void validateDate(String date) throws ApplicationException {
    Long dateToValidate = DateUtils.convertDateToLong(date);
    Long currentDate = new LocalDate().toDate().getTime();
    if (dateToValidate < currentDate) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_PROGRAM_DATE);
    }
  }

  private Boolean validateStartDateAndEndDate(String startDate, String endDate)
      throws ApplicationException {
    Long startDateEpoch = DateUtils.convertDateToLong(startDate);
    Long endDateEpoch = DateUtils.convertDateToLong(endDate);
    return startDateEpoch >= endDateEpoch;
  }

  @Override
  public Page<Program> getPrograms(GetProgramFilter filter) throws ApplicationException {
    return this.programRepository.getPrograms(filter);
  }

  @Override
  public Program updateProgram(String userId, String programId, UpdateProgramRequest request)
      throws ApplicationException {
    Program program = this.programRepository.findByIdAndMarkForDeleteFalse(programId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.PROGRAM_NOT_FOUND));
    UserAccount userAccount = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(userId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    
    if (program.isLockedProgram() && !UserAccountRole.SUPERVISOR.equals(userAccount.getRole())) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.PROGRAM_IS_LOCKED);
    }

    this.validateDate(request.getStartDate());
    this.validateDate(request.getEndDate());
    if (this.validateStartDateAndEndDate(request.getStartDate(), request.getEndDate())) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.INVALID_START_DATE_END_DATE);
    }
    
    program.setSubsidy(request.getSubsidy());
    program.setBudget(request.getBudget());
    program.setName(request.getName());
    program.setStartDate(DateUtils.dateInputToTimestamp(request.getStartDate()));
    program.setEndDate(DateUtils.dateInputToTimestamp(request.getEndDate()));
    return this.programRepository.save(program);
  }

  @Override
  public Program lockProgram(String userId, String programId) throws ApplicationException {
    Program program = this.programRepository.findByIdAndMarkForDeleteFalse(programId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.PROGRAM_NOT_FOUND));
    UserAccount userAccount = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(userId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));

    if (!UserAccountRole.SUPERVISOR.equals(userAccount.getRole())) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.PROGRAM_HANDLING_IS_PROHIBITED);
    }
    
    program.setLockedProgram(true);
    return this.programRepository.save(program);
  }

  @Override
  public Boolean deleteProgram(String programId) throws ApplicationException {
    Program program = this.programRepository.findByIdAndMarkForDeleteFalse(programId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.PROGRAM_NOT_FOUND));
    program.setMarkForDelete(true);
    this.programRepository.save(program);
    return Boolean.TRUE;
  }
}
