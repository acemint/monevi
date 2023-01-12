package com.monevi.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.monevi.properties.JavaMailProperties;

@Configuration
public class JavaMailConfiguration {

  private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
  private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
  private static final String MAIL_DEBUG = "mail.debug";
  private static final String SMTP = "smtp";

  @Autowired
  private JavaMailProperties javaMailProperties;

  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(javaMailProperties.getHost());
    mailSender.setPort(javaMailProperties.getPort());

    mailSender.setUsername(javaMailProperties.getUsername());
    mailSender.setPassword(javaMailProperties.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put(MAIL_TRANSPORT_PROTOCOL, SMTP);
    props.put(MAIL_SMTP_AUTH, javaMailProperties.getSmtpAuth());
    props.put(MAIL_SMTP_STARTTLS_ENABLE, javaMailProperties.getSmtpStartTlsEnable());
    props.put(MAIL_DEBUG, javaMailProperties.getMailDebug());

    return mailSender;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(thymeleafTemplateResolver());
    return templateEngine;
  }

  @Bean
  public ITemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix(javaMailProperties.getTemplatePath());
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    return viewResolver;
  }
}
