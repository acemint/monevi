package com.monevi.converter;

import com.monevi.dto.response.UserAccountResponse;
import com.monevi.entity.UserAccount;
import org.springframework.stereotype.Component;

@Component(value = UserAccountToUserAccountResponseConverter.COMPONENT_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class UserAccountToUserAccountResponseConverter implements Converter<UserAccount, UserAccountResponse> {

    public static final String COMPONENT_NAME = "UserAccountToUserAccountResponse";

    @Override
    public UserAccountResponse convert(UserAccount source) {
        return UserAccountResponse.builder()
            .id(source.getId())
            .nim(source.getNim())
            .fullName(source.getFullName())
            .email(source.getEmail())
            .role(source.getRole().name())
            .periodMonth(source.getPeriodMonth())
            .periodYear(source.getPeriodYear())
            .lockedAccount(source.getLockedAccount())
            .build();
    }
}
