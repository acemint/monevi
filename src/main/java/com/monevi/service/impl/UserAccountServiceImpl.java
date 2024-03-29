package com.monevi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.monevi.dto.request.SendEmailRequest;
import com.monevi.enums.MessageTemplate;
import com.monevi.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monevi.configuration.PasswordEncoderConfiguration;
import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Region;
import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetStudentFilter;
import com.monevi.repository.OrganizationRepository;
import com.monevi.repository.RegionRepository;
import com.monevi.repository.UserAccountRepository;
import com.monevi.service.AuthService;
import com.monevi.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {

  @Value("${monevi.redirect.login.url}")
  private String loginUrl;
  
  private static final String USER_NAME = "name";
  private static final String URL_KEY = "url";

  @Autowired
  private PasswordEncoderConfiguration passwordEncoder;

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Autowired
  private AuthService authService;
  
  @Autowired
  private MessageService messageService;

  @Override
  public UserAccount register(CreateStudentRequest request) throws ApplicationException {
    Optional<UserAccount> nimStudent =
        this.userAccountRepository.findByNimAndMarkForDeleteIsFalse(request.getNim());
    if (nimStudent.isPresent()) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.NIM_ALREADY_EXISTS);
    }

    Optional<UserAccount> emailStudent =
        this.userAccountRepository.findByEmailAndMarkForDeleteFalse(request.getEmail());
    if (emailStudent.isPresent()) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST,
          ErrorMessages.EMAIL_ALREADY_REGISTERED);
    }

    Organization organization =
        this.organizationRepository.findByNameAndMarkForDeleteIsFalse(request.getOrganizationName())
            .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
                ErrorMessages.ORGANIZATION_DOES_NOT_EXIST));

    OrganizationRegion organizationRegion = organization.getOrganizationRegions().stream()
        .filter(or -> filterByRegionNameAndMarkForDeleteFalse(or, request.getRegionName()))
        .findFirst().orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    
    this.checkIsAssignedUserExists(request.getPeriodMonth(), request.getPeriodYear(),
        organizationRegion.getId(), UserAccountRole.valueOf(request.getRole()));

    UserAccount newStudent = new UserAccount();
    newStudent.setNim(request.getNim());
    newStudent.setEmail(request.getEmail());
    newStudent.setFullName(request.getFullName());
    newStudent.setPeriodMonth(request.getPeriodMonth());
    newStudent.setPeriodYear(request.getPeriodYear());
    newStudent.setRole(UserAccountRole.valueOf(request.getRole()));
    newStudent.setPassword(this.passwordEncoder.encoder().encode(request.getPassword()));
    newStudent.setOrganizationRegion(organizationRegion);
    newStudent.setLockedAccount(true);

    this.userAccountRepository.save(newStudent);
    return newStudent;
  }

  private boolean filterByRegionNameAndMarkForDeleteFalse(OrganizationRegion organizationRegion,
      String regionName) {
    return organizationRegion.getMarkForDelete().equals(Boolean.FALSE)
        && organizationRegion.getRegion().getName().equals(regionName);
  }

  private void checkIsAssignedUserExists(Integer periodMonth, Integer periodYear,
      String organizationRegionId, UserAccountRole role) throws ApplicationException {
    Optional<UserAccount> studentWithRole = this.userAccountRepository
        .findAssignedUserByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(periodMonth, periodYear,
            organizationRegionId, role);
    if (studentWithRole.isPresent()) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST,
          String.format(ErrorMessages.ROLE_ALREADY_TAKEN, role.getName()));
    }
  }

  @Override
  public UserAccount register(CreateSupervisorRequest supervisor) throws ApplicationException {
    Optional<UserAccount> emailSupervisor =
        this.userAccountRepository.findByEmailAndMarkForDeleteFalse(supervisor.getEmail());
    if (emailSupervisor.isPresent()) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST,
          ErrorMessages.EMAIL_ALREADY_REGISTERED);
    }

    Region region =
        this.regionRepository.findByNameAndMarkForDeleteIsFalse(supervisor.getRegionName())
            .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
                ErrorMessages.REGION_DOES_NOT_EXIST));

    UserAccount newSupervisor = new UserAccount();
    newSupervisor.setFullName(supervisor.getFullName());
    newSupervisor.setEmail(supervisor.getEmail());
    newSupervisor.setRole(UserAccountRole.SUPERVISOR);
    newSupervisor.setRegion(region);
    newSupervisor.setLockedAccount(false);
    newSupervisor.setPassword(this.passwordEncoder.encoder().encode(supervisor.getPassword()));

    this.userAccountRepository.save(newSupervisor);
    this.authService.generateResetPasswordToken(supervisor.getEmail(),
        MessageTemplate.ACCOUNT_VERIFICATION);
    return newSupervisor;
  }

  @Override
  public UserAccount approveStudent(String studentId) throws ApplicationException {
    UserAccount user = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(studentId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    this.checkIsAssignedUserExists(user.getPeriodMonth(), user.getPeriodYear(),
        user.getOrganizationRegion().getId(), user.getRole());

    if (user.getLockedAccount()) {
      user.setLockedAccount(Boolean.FALSE);
      this.userAccountRepository.save(user);

      Optional<List<UserAccount>> listOfDeclinedUser = this.userAccountRepository
          .findAllByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(user.getPeriodMonth(),
              user.getPeriodYear(), user.getOrganizationRegion().getId(), user.getRole());
      if (listOfDeclinedUser.isPresent()) {
        List<UserAccount> deletedList = listOfDeclinedUser.get().stream()
            .map(this::setUserMarkForDeleteTrue).collect(Collectors.toList());
        this.userAccountRepository.saveAll(deletedList);
      }
    }
    this.sendEmailToStudent(user.getEmail(), user.getFullName());
    return user;
  }

  private UserAccount setUserMarkForDeleteTrue(UserAccount userAccount) {
    userAccount.setMarkForDelete(Boolean.TRUE);
    return userAccount;
  }
  
  private void sendEmailToStudent(String studentEmail, String studentName)
      throws ApplicationException {
    Map<String, String> variables = new HashMap<>();
    variables.put(USER_NAME, StringUtils.split(studentName, " ")[0]);
    variables.put(URL_KEY, loginUrl);
    SendEmailRequest request =
        SendEmailRequest.builder()
            .messageTemplateId(MessageTemplate.APPROVED_ACCOUNT)
            .variables(variables)
            .recipient(studentEmail).build();
    this.messageService.sendEmail(request);
  }

  @Override
  public UserAccount declineStudent(String studentId) throws ApplicationException {
    UserAccount user = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(studentId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    user.setMarkForDelete(Boolean.TRUE);
    this.userAccountRepository.save(user);
    return user;
  }

  @Override
  public Page<UserAccount> findAllStudentByFilter(GetStudentFilter filter)
      throws ApplicationException {
    if (Objects.nonNull(filter.getPeriodMonth())
        && (filter.getPeriodMonth() < 1 || filter.getPeriodMonth() > 12)) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_MONTH);
    }
    if (Objects.nonNull(filter.getStudentRole())
        && UserAccountRole.SUPERVISOR.equals(filter.getStudentRole())) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_STUDENT_ROLE);
    }
    return this.userAccountRepository.findAllStudentByFilter(filter);
  }
}
