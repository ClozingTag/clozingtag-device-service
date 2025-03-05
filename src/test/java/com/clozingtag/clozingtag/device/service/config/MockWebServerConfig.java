package com.clozingtag.clozingtag.device.service.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
public class MockWebServerConfig {

    public static MockWebServer mockWebServer;

    @PostConstruct
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8089);
    }

    @PreDestroy
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws IOException {
        ClassPathResource resource = new ClassPathResource("jwks.json");
        String jwks = new String(Objects.requireNonNull(resource.getInputStream()).readAllBytes(), StandardCharsets.UTF_8);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(jwks));


        return NimbusJwtDecoder.withJwkSetUri(mockWebServer.url("/jwks").toString()).build();
    }
}