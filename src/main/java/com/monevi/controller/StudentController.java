package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.StudentToStudentResponseConverter;
import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.StudentResponse;
import com.monevi.entity.Student;
import com.monevi.exception.ApplicationException;
import com.monevi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(StudentControllerPath.BASE)
public class StudentController {

  @Autowired
  private StudentService studentService;

  @Autowired
  private Converter<Student, StudentResponse> converter;

  @PostMapping(StudentControllerPath.ADD_NEW)
  public BaseResponse<StudentResponse> createNewMember(@Valid @RequestBody CreateStudentRequest request)
          throws ApplicationException {
     Student student = this.studentService.register(request);
     return BaseResponse.<StudentResponse>builder()
             .value(this.converter.convert(student))
             .build();
  }
}
