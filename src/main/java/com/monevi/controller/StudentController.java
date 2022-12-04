package com.monevi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(StudentControllerPath.BASE)
public class StudentController {

  @GetMapping(StudentControllerPath.GET)
  public String helloWorld() {
    return "Hello World!";
  }

  @PostMapping(StudentControllerPath.ADD_NEW)
  public String createNewMember() {
    return "NEW MEMBER!";
  }

}
