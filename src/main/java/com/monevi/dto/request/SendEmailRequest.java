package com.monevi.dto.request;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.monevi.constant.ErrorMessages;
import com.monevi.enums.MessageTemplate;
import com.monevi.validation.annotation.ValidEmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {

  @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private MessageTemplate messageTemplateId;

  @ValidEmail
  private String recipient;

  private Map<String, String> variables;
}
