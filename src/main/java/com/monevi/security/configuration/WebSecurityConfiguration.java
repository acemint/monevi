package com.monevi.security.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.monevi.configuration.PasswordEncoderConfiguration;
import com.monevi.security.jwt.AuthEntryPoint;
import com.monevi.security.jwt.AuthTokenFilter;
import com.monevi.security.service.UserDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {

  @Value("${monevi.cors.allowed-path}")
  private String allowedPath;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPoint unauthorizedHandler;

  @Autowired
  private PasswordEncoderConfiguration passwordEncoder;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder.encoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration)
      throws Exception {
    return authConfiguration.getAuthenticationManager();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers("/js/**", "/images/**");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().configurationSource(corsConfigurationSource())
        .and().csrf().disable().exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers("/api/monevi/region/**", "/api/monevi/organization/all", "/api/monevi/auth/**")
        .permitAll()
        .antMatchers("/api/monevi/transaction/**",
            "/api/monevi/user/**",
            "/api/monevi/report/**",
            "/api/monevi/program/**",
            "/api/monevi/organization/**",
            "/api/monevi/wallet/**")
        .permitAll();
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(),
        UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean("corsConfigurationSource")
  public CorsConfigurationSource corsConfigurationSource() {
    List<String> listOfAllowedMethod = Arrays.asList("GET", "PUT", "POST", "DELETE");
    List<String> listOfAllowedHeaders =
        Arrays.asList("Authorization", "Content-Type", "Cache-Control");
    List<String> listOfAllowedOrigins =
        Arrays.stream(StringUtils.split(allowedPath, ",")).collect(Collectors.toList());
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(listOfAllowedOrigins);
    configuration.setAllowedMethods(listOfAllowedMethod);
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(listOfAllowedHeaders);

    UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
