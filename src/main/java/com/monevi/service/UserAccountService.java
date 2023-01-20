package com.monevi.service;

import java.util.List;

import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.entity.UserAccount;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetStudentFilter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;

public interface UserAccountService {
    UserAccount register(CreateStudentRequest request) throws ApplicationException;

    UserAccount register(CreateSupervisorRequest request) throws ApplicationException;

    UserAccount approveStudent(String studentId) throws ApplicationException;

    UserAccount declineStudent(String studentId) throws ApplicationException;

   Page<UserAccount> findAllStudentByFilter(GetStudentFilter filter)
        throws ApplicationException;
}
