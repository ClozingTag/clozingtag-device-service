package com.clozingtag.clozingtag.device.service.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Map;

@Data
@ConfigurationProperties("device")
@RefreshScope
public class AppConfiguration {

  private String msg;

  private String buildVersion;

  private String baseUrl;

  private MailJetConfig mailjet;

  @Data
  public static class MailJetConfig {
    private MailjetApi api;

    private MailJetSender sender;

    private Map<String, String> templates;

    private String fromEmail;

    private String fromName;
  }


  @Data
  public static class MailjetApi {
    private String key;

    private String secret;
  }


  @Data
  public static class MailJetSender {

    private String email;

    private String name;
  }
}
