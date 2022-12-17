package com.monevi.service;

import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.entity.Student;
import com.monevi.exception.ApplicationException;

public interface StudentService {
    Student register(CreateStudentRequest student) throws ApplicationException;
}
