package com.monevi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class JavaMailProperties {

  private String host;
  private int port;
  private String username;
  private String password;
  private String smtpAuth;
  private String smtpStartTlsEnable;
  private String mailDebug;
  private String templatePath;
}
