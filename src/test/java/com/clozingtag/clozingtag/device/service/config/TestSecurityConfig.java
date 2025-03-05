package com.clozingtag.clozingtag.device.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@Profile("test")
@Import(MockWebServerConfig.class)
public class TestSecurityConfig {
    @Autowired
    private MockWebServerConfig mockWebServerConfig;

    @Primary
    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return mockWebServerConfig.jwtDecoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/test/**")// Disable CSRF,
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated() // Require authentication for all requests first
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}