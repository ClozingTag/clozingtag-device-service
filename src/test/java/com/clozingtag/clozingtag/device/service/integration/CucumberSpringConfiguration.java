package com.clozingtag.clozingtag.device.service.integration;

import com.clozingtag.clozingtag.device.service.config.MockWebServerConfig;
import com.clozingtag.clozingtag.device.service.config.TestSecurityConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({MockWebServerConfig.class, TestSecurityConfig.class})
public class CucumberSpringConfiguration {

}