package com.monevi.service.impl;

import java.util.Map;
import java.util.Objects;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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

  @Value("${spring.mail.username}")
  private String sender;

  @Override
  public SendMessageResponse sendEmail(SendEmailRequest request) {
    try {
      final MimeMessage mimeMessage = this.javaMailSender.mailSender().createMimeMessage();
      final MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      final Context ctx = new Context();
      for(Map.Entry<String, String> variable: request.getVariables().entrySet()) {
        ctx.setVariable(variable.getKey(), variable.getValue());
      }

      String htmlBody = javaMailSender.templateEngine()
          .process(request.getMessageTemplateId().getTemplateFile(), ctx);

      mailMessage.setFrom(new InternetAddress(sender));
      mailMessage.setTo(request.getRecipient());
      mailMessage.setText(htmlBody, true);
      mailMessage.setSubject(toSubject(request.getMessageTemplateId(), request.getVariables()));

      ClassPathResource clr = new ClassPathResource(MONEVI_LOGO_PNG);
      mailMessage.addInline(MONEVI_LOGO, clr, PNG_CONTENT_TYPE);

      javaMailSender.mailSender().send(mimeMessage);
      return toSendMessageResponse(request);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email");
    }
  }

  private String toSubject(MessageTemplate messageTemplateId, Map<String, String> variables) {
    if (Objects.nonNull(messageTemplateId)) {
      if (MessageTemplate.DECILNED_REPORT.equals(messageTemplateId)
          || MessageTemplate.APPROVED_REPORT.equals(messageTemplateId)) {
        return String.format(messageTemplateId.getSubject(), variables.get(REPORT_MONTH),
            variables.get(REPORT_YEAR));
      } else if (MessageTemplate.SUBMITTED_REPORT.equals(messageTemplateId)) {
        return String.format(messageTemplateId.getSubject(), variables.get(ORGANIZATION_NAME),
            variables.get(REPORT_MONTH), variables.get(REPORT_YEAR));
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
