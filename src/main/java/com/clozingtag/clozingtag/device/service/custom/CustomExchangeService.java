package com.clozingtag.clozingtag.device.service.custom;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@HttpExchange("/v1")
public interface CustomExchangeService {

  @PutExchange(value = "/auth/guests/app/abuse")
  ResponseEntity handlePlatformAbuse(
      @RequestBody List<String> userIds, @RequestParam String organizerAuthUserId);

  @GetExchange("/auth/guests/token/{id}")
  ResponseEntity getUserDeviceToken(@PathVariable String id);
}
