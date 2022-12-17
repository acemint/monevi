package com.monevi.repository;

import com.monevi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String>  {
    Optional<Student> findByNimAndMarkForDeleteIsFalse(String nim);
    Optional<Student> findByEmailAndMarkForDeleteFalse(String email);
}
