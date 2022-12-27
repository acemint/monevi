package com.monevi.converter;

import com.monevi.dto.response.SupervisorResponse;
import com.monevi.entity.Supervisor;
import org.springframework.stereotype.Component;

@Component(value = SupervisorToSupervisorResponseConverter.SUPERVISOR_TO_SUPERVISOR_RESPONSE_BEAN_NAME
        + Converter.SUFFIX_BEAN_NAME)
public class SupervisorToSupervisorResponseConverter implements Converter<Supervisor, SupervisorResponse> {
    public static final String SUPERVISOR_TO_SUPERVISOR_RESPONSE_BEAN_NAME = "SupervisorToSupervisorResponse";

    @Override
    public SupervisorResponse convert(Supervisor source) {
        return SupervisorResponse.builder()
                .fullName(source.getFullName())
                .email(source.getEmail())
                .build();
    }
}
