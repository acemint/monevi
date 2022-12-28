package com.monevi.service.impl;

import com.monevi.configuration.PasswordEncoderConfiguration;
import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.UserAccount;
import com.monevi.entity.Terms;
import com.monevi.enums.StudentRole;
import com.monevi.enums.UserAccountType;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.OrganizationRepository;
import com.monevi.repository.UserAccountRepository;
import com.monevi.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private PasswordEncoderConfiguration passwordEncoder;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public UserAccount register(CreateStudentRequest request) throws ApplicationException {
        Optional<UserAccount> nimStudent =
                this.userAccountRepository.findByNimAndMarkForDeleteIsFalse(request.getNim());
        if (nimStudent.isPresent()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.NIM_ALREADY_EXIST);
        }

        Optional<UserAccount> emailStudent =
            this.userAccountRepository.findByEmailAndMarkForDeleteFalse(request.getEmail());
        if (emailStudent.isPresent()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_ALREADY_REGISTERED);
        }

        Organization organization =
            this.organizationRepository.findByNameAndMarkForDeleteIsFalse(request.getOrganizationName())
                .orElseThrow(() ->
                    new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_DOES_NOT_EXIST));

        OrganizationRegion organizationRegion = organization.getOrganizationRegions().stream()
            .filter(or -> filterByRegionNameAndMarkForDeleteFalse(or, request.getRegionName()))
            .findFirst().orElseThrow(() ->
                new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));

        UserAccount newStudent = new UserAccount();
        newStudent.setNim(request.getNim());
        newStudent.setEmail(request.getEmail());
        newStudent.setFullName(request.getFullName());
        newStudent.setType(UserAccountType.STUDENT);
        newStudent.setPassword(this.passwordEncoder.encoder().encode(request.getPassword()));

        Terms currentTerm = new Terms();
        currentTerm.setUserAccount(newStudent);
        currentTerm.setPeriodMonth(request.getPeriodMonth());
        currentTerm.setPeriodYear(request.getPeriodYear());
        currentTerm.setRole(StudentRole.valueOf(request.getStudentRole()));
        currentTerm.setOrganizationRegion(organizationRegion);
        currentTerm.setLockedAccount(true);

        Set<Terms> terms = new HashSet<>();
        terms.add(currentTerm);
        newStudent.setTerms(terms);

        this.userAccountRepository.save(newStudent);
        return newStudent;
    }

    private boolean filterByRegionNameAndMarkForDeleteFalse(OrganizationRegion organizationRegion, String regionName) {
        return organizationRegion.getMarkForDelete().equals(Boolean.FALSE)
                && organizationRegion.getRegion().getName().equals(regionName);
    }
    @Override
    public UserAccount register(CreateSupervisorRequest supervisor) throws ApplicationException {
        Optional<UserAccount> emailSupervisor =
            this.userAccountRepository.findByEmailAndMarkForDeleteFalse(supervisor.getEmail());
        if(emailSupervisor.isPresent()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_ALREADY_REGISTERED);
        }

        UserAccount newSupervisor = new UserAccount();
        newSupervisor.setFullName(supervisor.getFullName());
        newSupervisor.setEmail(supervisor.getEmail());
        newSupervisor.setType(UserAccountType.SUPERVISOR);
        newSupervisor.setPassword(this.passwordEncoder.encoder().encode(supervisor.getPassword()));

        this.userAccountRepository.save(newSupervisor);
        return newSupervisor;
    }
}
