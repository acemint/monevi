package com.monevi.converter;

import org.springframework.stereotype.Component;

import com.monevi.dto.response.StudentFindAllResponse;
import com.monevi.entity.UserAccount;

@Component(value = UserAccountToStudentFindAllResponseConverter.COMPONENT_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class UserAccountToStudentFindAllResponseConverter
    implements Converter<UserAccount, StudentFindAllResponse> {

  public static final String COMPONENT_NAME = "UserAccountToStudentFindAllResponse";

  @Override
  public StudentFindAllResponse convert(UserAccount source) {
    return StudentFindAllResponse.builder()
        .nim(source.getNim())
        .fullname(source.getFullName())
        .email(source.getEmail())
        .orgName(source.getOrganizationRegion().getOrganization().getName())
        .orgAbbreviation(source.getOrganizationRegion().getOrganization().getAbbreviation())
        .regionName(source.getOrganizationRegion().getRegion().getName())
        .periodYear(source.getPeriodYear())
        .periodMonth(source.getPeriodMonth())
        .role(source.getRole().name())
        .lockedAccount(source.getLockedAccount()).build();
  }
}
