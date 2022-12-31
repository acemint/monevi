package com.monevi.service;

import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;

public interface ProgramService {

  Program create(CreateProgramRequest request) throws ApplicationException;

}
