package com.monevi.repository;

import com.monevi.entity.Program;
import com.monevi.model.GetProgramFilter;

import java.util.List;
import java.util.Optional;

public interface ProgramCustomRepository {

  Optional<List<Program>> getPrograms(GetProgramFilter filter);

}
