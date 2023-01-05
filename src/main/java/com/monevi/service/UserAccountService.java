package com.monevi.service;

import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.entity.UserAccount;
import com.monevi.exception.ApplicationException;

public interface UserAccountService {
    UserAccount register(CreateStudentRequest request) throws ApplicationException;

    UserAccount register(CreateSupervisorRequest request) throws ApplicationException;

    UserAccount approveStudent(String studentId) throws ApplicationException;

    UserAccount declineStudent(String studentId) throws ApplicationException;
}
