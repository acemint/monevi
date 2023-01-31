package com.monevi.controller;

import javax.validation.Valid;

import com.monevi.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monevi.dto.request.SendEmailRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.SendMessageResponse;
import com.monevi.service.MessageService;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.ORGANIZATION)
@Validated
public class MessageController {

  @Autowired
  private MessageService messageService;

  @PostMapping(value = ApiPath.SEND_EMAIL, consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<SendMessageResponse> authenticateUser(
      @Valid @RequestBody SendEmailRequest request) throws ApplicationException {
    SendMessageResponse response = this.messageService.sendEmail(request);
    return BaseResponse.<SendMessageResponse>builder().value(response).build();
  }
}
