package com.clozingtag.clozingtag.device.service.custom;


import com.clozingtag.clozingtag.device.service.dto.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@HttpExchange("/api")
public interface CustomExchangeService {

  //TODO
  // use kafka instead of http exchange
  // if you have a lot of messages to send or other data to be sent
  @PostExchange(value = "/notification/v1/notifications")
  void createNotification(@RequestBody NotificationRequest request);

}
