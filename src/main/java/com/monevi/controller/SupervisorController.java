package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.SupervisorResponse;
import com.monevi.entity.Supervisor;
import com.monevi.exception.ApplicationException;
import com.monevi.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.SUPERVISOR)
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private Converter<Supervisor, SupervisorResponse> supervisorToSupervisorResponseConverter;

    @PostMapping(ApiPath.REGISTER)
    public BaseResponse<SupervisorResponse> createNewMember(@Valid @RequestBody CreateSupervisorRequest request)
            throws ApplicationException {
        Supervisor supervisor = this.supervisorService.register(request);
        return BaseResponse.<SupervisorResponse>builder()
                .value(this.supervisorToSupervisorResponseConverter.convert(supervisor))
                .build();
    }
}
