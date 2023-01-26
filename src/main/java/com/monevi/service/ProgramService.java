package com.monevi.service;

import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.dto.request.UpdateProgramRequest;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetProgramFilter;
import org.springframework.data.domain.Page;

public interface ProgramService {

  Program create(CreateProgramRequest request) throws ApplicationException;

  Page<Program> getPrograms(GetProgramFilter filter) throws ApplicationException;

  Program updateProgram(String userId, String programId, UpdateProgramRequest request)
      throws ApplicationException;

  Program lockProgram(String userId, String programId) throws ApplicationException;

  Boolean deleteProgram(String programId) throws ApplicationException;
}
