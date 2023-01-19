package com.monevi.service;

import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.dto.request.UpdateSubsidyProgramRequest;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetProgramFilter;

import java.util.List;

public interface ProgramService {

  Program create(CreateProgramRequest request) throws ApplicationException;

  List<Program> getPrograms(GetProgramFilter filter) throws ApplicationException;

  Program updateSubsidy(String programId, UpdateSubsidyProgramRequest request)
      throws ApplicationException;

}
