package com.monevi.service;

import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.entity.Supervisor;
import com.monevi.exception.ApplicationException;

public interface SupervisorService {

    Supervisor register(CreateSupervisorRequest supervisor) throws ApplicationException;
}
