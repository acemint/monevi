package com.monevi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monevi.converter.Converter;
import com.monevi.converter.UserAccountToStudentFindAllResponseConverter;
import com.monevi.converter.UserAccountToUserAccountResponseConverter;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.StudentFindAllResponse;
import com.monevi.dto.response.UserAccountResponse;
import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetStudentFilter;
import com.monevi.service.UserAccountService;
import com.monevi.util.UserAccountUrlUtils;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.USER)
public class UserAccountController {

  @Autowired
  private UserAccountService userAccountService;

  @Autowired
  @Qualifier(UserAccountToUserAccountResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<UserAccount, UserAccountResponse> userAccountToUserAccountResponse;

  @Autowired
  @Qualifier(UserAccountToStudentFindAllResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<UserAccount, StudentFindAllResponse> userAccountStudentFindAllResponseConverter;

  // @PreAuthorize("hasRole('SUPERVISOR')")
  @PostMapping(value = ApiPath.SUPERVISOR + ApiPath.APPROVE_ACCOUNT)
  public BaseResponse<UserAccountResponse> approveStudent(@RequestParam @NotBlank String studentId)
      throws ApplicationException {
    UserAccount userAccount = this.userAccountService.approveStudent(studentId);
    return BaseResponse.<UserAccountResponse>builder()
        .value(this.userAccountToUserAccountResponse.convert(userAccount)).build();
  }

  // @PreAuthorize("hasRole('SUPERVISOR')")
  @PostMapping(value = ApiPath.SUPERVISOR + ApiPath.DECLINE_ACCOUNT)
  public BaseResponse<UserAccountResponse> declineAccount(@RequestParam @NotBlank String studentId)
          throws ApplicationException {
    UserAccount userAccount = this.userAccountService.declineStudent(studentId);
    return BaseResponse.<UserAccountResponse>builder()
            .value(this.userAccountToUserAccountResponse.convert(userAccount)).build();
  }

  @GetMapping(value = ApiPath.FIND_ALL_STUDENT, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<StudentFindAllResponse> findAllStudentByFilter(
      @RequestParam(required = false) String studentName,
      @RequestParam(required = false) String organizationName,
      @RequestParam(required = false) String regionId,
      @RequestParam(required = false) Integer periodMonth,
      @RequestParam(required = false) Integer periodYear,
      @RequestParam(required = false) UserAccountRole studentRole,
      @RequestParam(defaultValue = "true", required = false) Boolean lockedAccount,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "createdDate", required = false) String[] sortBy,
      @RequestParam(defaultValue = "false", required = false) String[] isAscending)
      throws ApplicationException {
    GetStudentFilter filter = this.buildGetStudentFilter(studentName, organizationName, regionId,
        periodMonth, periodYear, studentRole, lockedAccount, page, size, sortBy, isAscending);
    Page<UserAccount> pageResponse = this.userAccountService.findAllStudentByFilter(filter);
    List<StudentFindAllResponse> studentFindAllResponses = pageResponse.getContent().stream()
        .map(data -> this.userAccountStudentFindAllResponseConverter.convert(data))
        .collect(Collectors.toList());
    return MultipleBaseResponse.<StudentFindAllResponse>builder()
        .values(studentFindAllResponses)
        .metadata(MultipleBaseResponse.Metadata.builder()
            .size(studentFindAllResponses.size())
            .totalPage(pageResponse.getTotalPages())
            .totalItems(pageResponse.getTotalElements())
            .build())
        .build();
  }

  private GetStudentFilter buildGetStudentFilter(String studentName, String organizationName,
      String regionId, Integer periodMonth, Integer periodYear, UserAccountRole studentRole,
      Boolean lockedAccount, int page, int size, String[] sortBy, String[] isAscending)
      throws ApplicationException {
    List<Sort.Order> sortOrders = new ArrayList<>();
    int validSize = Math.min(sortBy.length, isAscending.length);
    for (int i = 0; i < validSize; i++) {
      UserAccountUrlUtils.checkValidSortedBy(sortBy[i]);
      sortOrders
          .add(new Sort.Order(UserAccountUrlUtils.getSortDirection(isAscending[i]), sortBy[i]));
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
    return GetStudentFilter.builder()
        .studentName(studentName)
        .organizationName(organizationName)
        .regionId(regionId)
        .periodMonth(periodMonth)
        .periodYear(periodYear)
        .studentRole(studentRole)
        .lockedAccount(lockedAccount)
        .pageable(pageable).build();
  }
}
