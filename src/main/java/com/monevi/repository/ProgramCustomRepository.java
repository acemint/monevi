package com.monevi.repository;

import com.monevi.entity.Program;
import com.monevi.model.GetProgramFilter;
import org.springframework.data.domain.Page;

public interface ProgramCustomRepository {

  Page<Program> getPrograms(GetProgramFilter filter);

}
