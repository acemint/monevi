package com.monevi.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration{

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();

    String herokuDbUrl = "jdbc:postgresql://" + dbUrl.split("//")[1];
    config.setJdbcUrl(herokuDbUrl);
    return new HikariDataSource(config);
  }
}