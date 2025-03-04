package com.clozingtag.clozingtag.device.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeviceStateException extends RuntimeException {
    public DeviceStateException(String message) {
        super(message);
    }
}