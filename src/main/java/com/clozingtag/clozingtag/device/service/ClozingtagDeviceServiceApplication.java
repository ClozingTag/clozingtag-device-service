package com.clozingtag.clozingtag.device.service;

import com.clozingtag.clozingtag.device.service.configuration.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableConfigurationProperties(value = AppConfiguration.class)
@RefreshScope
public class ClozingtagDeviceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClozingtagDeviceServiceApplication.class, args);
	}

}
