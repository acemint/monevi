package com.monevi.converter;

import com.monevi.dto.response.StudentResponse;
import com.monevi.entity.Student;
import org.springframework.stereotype.Component;

@Component(value = StudentToStudentResponseConverter.STUDENT_TO_STUDENT_RESPONSE_BEAN_NAME
        + Converter.SUFFIX_BEAN_NAME)
public class StudentToStudentResponseConverter implements Converter<Student, StudentResponse> {

    public static final String STUDENT_TO_STUDENT_RESPONSE_BEAN_NAME = "StudentToStudentResponse";

    @Override
    public StudentResponse convert(Student source) {
        return StudentResponse.builder()
                .nim(source.getNim())
                .fullName(source.getFullName())
                .email(source.getEmail())
                .build();
    }
}
