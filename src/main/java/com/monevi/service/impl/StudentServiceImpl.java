package com.monevi.service.impl;

import com.monevi.configuration.PasswordEncoderConfiguration;
import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Student;
import com.monevi.entity.Terms;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.OrganizationRepository;
import com.monevi.repository.StudentRepository;
import com.monevi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private PasswordEncoderConfiguration passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Student register(CreateStudentRequest student) throws ApplicationException {
        Optional<Student> nimStudent =
                this.studentRepository.findByNimAndMarkForDeleteIsFalse(student.getNim());
        if(nimStudent.isPresent()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.NIM_ALREADY_EXIST);
        }

        Optional<Student> emailStudent =
                this.studentRepository.findByEmailAndMarkForDeleteFalse(student.getEmail());
        if(emailStudent.isPresent() && student.getEmail().equals(emailStudent.get().getEmail())) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_ALREADY_REGISTERED);
        }

        Organization organization =
                this.organizationRepository.findByNameAndMarkForDeleteIsFalse(student.getOrganization())
                        .orElseThrow(() ->
                                new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_DOES_NOT_EXIST));

        OrganizationRegion organizationRegion = organization.getOrganizationRegions().stream()
                .filter(organizationRegion1 -> organizationRegion1.getRegion().getName().equals(student.getRegion()))
                .findFirst().orElseThrow(() ->
                        new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));

        Student newStudent = new Student();
        newStudent.setNim(student.getNim());
        newStudent.setEmail(student.getEmail());
        newStudent.setFullName(student.getFullName());
        newStudent.setPassword(this.passwordEncoder.encoder().encode(student.getPassword()));

        Terms currentTerm = new Terms();
        currentTerm.setStudent(newStudent);
        currentTerm.setPeriodMonth(student.getPeriodMonth());
        currentTerm.setPeriodYear(student.getPeriodYear());
        currentTerm.setRole(student.getRole());
        currentTerm.setOrganizationRegion(organizationRegion);
        currentTerm.setLockedAccount(true);

        Set<Terms> terms = new HashSet<>();
        terms.add(currentTerm);
        newStudent.setTerms(terms);

        this.studentRepository.save(newStudent);
        return newStudent;
    }

}
