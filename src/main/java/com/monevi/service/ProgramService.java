package com.monevi.service;

import com.monevi.dto.request.CreateProgramRequest;
import com.monevi.entity.Program;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetProgramFilter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProgramService {

  Program create(CreateProgramRequest request) throws ApplicationException;

  Page<Program> getPrograms(GetProgramFilter filter) throws ApplicationException;

}
