package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.StudentResponse;
import com.monevi.entity.Student;
import com.monevi.exception.ApplicationException;
import com.monevi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.STUDENT)
public class StudentController {

  @Autowired
  private StudentService studentService;

  @Autowired
  private Converter<Student, StudentResponse> studentToStudentResponseConverter;

  @PostMapping(ApiPath.REGISTER)
  public BaseResponse<StudentResponse> createNewMember(@Valid @RequestBody CreateStudentRequest request)
          throws ApplicationException {
     Student student = this.studentService.register(request);
     return BaseResponse.<StudentResponse>builder()
             .value(this.studentToStudentResponseConverter.convert(student))
             .build();
  }
}
