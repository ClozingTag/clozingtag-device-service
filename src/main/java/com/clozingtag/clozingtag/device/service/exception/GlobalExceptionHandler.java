package com.clozingtag.clozingtag.device.service.exception;


import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.EnumSet;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<String> handleDeviceNotFoundException(DeviceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeviceStateException.class)
    public ResponseEntity<String> handleDeviceInUseException(DeviceStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEnumValue(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid state value: " + ex.getValue() + ". Valid values are: " + EnumSet.allOf(DeviceState.class);
        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
