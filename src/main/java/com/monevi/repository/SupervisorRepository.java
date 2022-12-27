package com.monevi.repository;

import com.monevi.entity.Student;
import com.monevi.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupervisorRepository extends JpaRepository<Supervisor, String> {

    Optional<Supervisor> findByEmailAndMarkForDeleteFalse(String email);
}
