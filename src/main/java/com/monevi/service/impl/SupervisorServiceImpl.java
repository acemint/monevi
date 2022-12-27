package com.monevi.service.impl;

import com.monevi.configuration.PasswordEncoderConfiguration;
import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.entity.Supervisor;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.SupervisorRepository;
import com.monevi.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private PasswordEncoderConfiguration passwordEncoder;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Override
    public Supervisor register(CreateSupervisorRequest supervisor) throws ApplicationException {
        Optional<Supervisor> emailSupervisor =
                this.supervisorRepository.findByEmailAndMarkForDeleteFalse(supervisor.getEmail());
        if(emailSupervisor.isPresent()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_ALREADY_REGISTERED);
        }

        Supervisor newSupervisor = new Supervisor();
        newSupervisor.setFullName(supervisor.getFullName());
        newSupervisor.setEmail(supervisor.getEmail());
        newSupervisor.setPassword(this.passwordEncoder.encoder().encode(supervisor.getPassword()));

        this.supervisorRepository.save(newSupervisor);
        return newSupervisor;
    }
}
