package com.clozingtag.clozingtag.device.service.configuration;

import com.clozingtag.clozingtag.device.service.custom.CustomJwtAuthenticationTokenConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain rsSecurityFilterChain(HttpSecurity http) throws Exception {

    return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers(WHITELIST)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwt ->
                        jwt.jwtAuthenticationConverter(
                            new CustomJwtAuthenticationTokenConverter())))
        .build();
  }

  private static final String[] WHITELIST = {
    "/webhooks/**",
    "/v3/api-docs",
    "/v3/api-docs/**",
    "/configuration/**",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/swagger-ui.html",
    "/api-docs/**",
    "/webjars/**"
  };

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.ignoring().requestMatchers("/webjars/**", "/image/**", "/guests/**", "/webhooks/**");
  }
}
