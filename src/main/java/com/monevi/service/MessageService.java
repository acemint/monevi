package com.monevi.service;

import com.monevi.dto.request.SendEmailRequest;
import com.monevi.dto.response.SendMessageResponse;
import com.monevi.enums.MessageTemplate;
import com.monevi.exception.ApplicationException;

public interface MessageService {

  SendMessageResponse sendEmail(SendEmailRequest request) throws ApplicationException;
}
