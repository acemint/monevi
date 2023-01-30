package com.monevi.service.impl;

import java.util.Map;
import java.util.Objects;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.monevi.constant.ErrorMessages;
import com.monevi.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.monevi.configuration.JavaMailConfiguration;
import com.monevi.dto.request.SendEmailRequest;
import com.monevi.dto.response.SendMessageResponse;
import com.monevi.enums.MessageTemplate;
import com.monevi.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

  private static final String MONEVI_LOGO_PNG = "templates/images/monevi-logo.png";
  private static final String MONEVI_LOGO = "moneviLogo";
  private static final String PNG_CONTENT_TYPE = "image/png";
  private static final String REPORT_MONTH = "reportMonth";
  private static final String REPORT_YEAR = "reportYear";
  private static final String ORGANIZATION_NAME = "organizationName";
  private static final String DEFAULT_SUBJECT = "Monevi";

  @Autowired
  private JavaMailConfiguration javaMailSender;

  @Value("${monevi.mail.sender}")
  private String sender;

  @Override
  public SendMessageResponse sendEmail(SendEmailRequest request) throws ApplicationException {
    try {
      final MimeMessage mimeMessage = this.javaMailSender.mailSender().createMimeMessage();
      final MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      final Context ctx = new Context();
      if (Objects.nonNull(request.getVariables())) {
        for (Map.Entry<String, String> variable : request.getVariables().entrySet()) {
          if (variable.getKey().equals("reportMonth")) {
            ctx.setVariable(variable.getKey(), this.convertMonth(variable.getValue()));
            continue;
          }
          ctx.setVariable(variable.getKey(), variable.getValue());
        }
      }

      String htmlBody = javaMailSender.templateEngine()
          .process(request.getMessageTemplateId().getTemplateFile(), ctx);

      mailMessage.setFrom(new InternetAddress(sender));
      mailMessage.setTo(InternetAddress.parse(request.getRecipient()));
      mailMessage.setText(htmlBody, true);
      mailMessage.setSubject(toSubject(request.getMessageTemplateId(), request.getVariables()));

      ClassPathResource clr = new ClassPathResource(MONEVI_LOGO_PNG);
      mailMessage.addInline(MONEVI_LOGO, clr, PNG_CONTENT_TYPE);

      javaMailSender.mailSender().send(mimeMessage);
      return toSendMessageResponse(request);
    } catch (Exception e) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.FAILED_TO_SEND_EMAIL);
    }
  }
  
  private String convertMonth(String month) {
    switch (month) {
      case "JANUARY":
        return "Januari";
      case "FEBRUARY":
        return "Februari";
      case "MARCH":
        return "Maret";
      case "MAY":
        return "Mei";
      case "JUNE":
        return "Juni";
      case "JULY":
        return "Juli";
      case "AUGUST":
        return "Agustus";
      case "OCTOBER":
        return "Oktober";
      case "DECEMBER":
        return "Desember";
      default:
        return month.charAt(0) + month.substring(1).toLowerCase();
    }
  }

  private String toSubject(MessageTemplate messageTemplateId, Map<String, String> variables) {
    if (Objects.nonNull(messageTemplateId)) {
      if (MessageTemplate.DECLINED_REPORT.equals(messageTemplateId)
          || MessageTemplate.APPROVED_REPORT.equals(messageTemplateId)) {
        return String.format(messageTemplateId.getSubject(),
            this.convertMonth(variables.get(REPORT_MONTH)), variables.get(REPORT_YEAR));
      } else if (MessageTemplate.SUBMITTED_REPORT.equals(messageTemplateId)) {
        return String.format(messageTemplateId.getSubject(), variables.get(ORGANIZATION_NAME),
            this.convertMonth(variables.get(REPORT_MONTH)), variables.get(REPORT_YEAR));
      } else {
        return messageTemplateId.getSubject();
      }
    } else {
      return DEFAULT_SUBJECT;
    }
  }
  
  private SendMessageResponse toSendMessageResponse(SendEmailRequest request) {
    return SendMessageResponse.builder()
        .sender(sender)
        .recipient(request.getRecipient())
        .messageType(request.getMessageTemplateId().name())
        .build();
  }
}
