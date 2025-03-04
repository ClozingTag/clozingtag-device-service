package com.clozingtag.clozingtag.device.service.configuration;

import com.clozingtag.clozingtag.device.service.custom.CustomExchangeService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfiguration {

  private final AppConfiguration appConfiguration;

  public WebClientConfiguration(AppConfiguration appConfiguration) {
    this.appConfiguration = appConfiguration;
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .exchangeStrategies(
            ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().enableLoggingRequestDetails(true))
                .build())
        .filter(new ServletBearerExchangeFilterFunction())
        .baseUrl(appConfiguration.getBaseUrl())
        .build();
  }

  @SneakyThrows
  @Bean
  CustomExchangeService customExchangeService(WebClient webClient) {
    HttpServiceProxyFactory httpServiceProxyFactory =
        HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
    return httpServiceProxyFactory.createClient(CustomExchangeService.class);
  }
}
