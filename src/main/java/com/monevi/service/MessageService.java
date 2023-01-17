package com.monevi.service;

import com.monevi.dto.request.SendEmailRequest;
import com.monevi.dto.response.SendMessageResponse;
import com.monevi.enums.MessageTemplate;

public interface MessageService {

  SendMessageResponse sendEmail(SendEmailRequest request);
}
