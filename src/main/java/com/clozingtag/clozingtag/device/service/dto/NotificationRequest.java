package com.clozingtag.clozingtag.device.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NotificationRequest {

    private Long userId;

    private String name;

    private String email;

    private String subject;

    private String body;

    private String template;

    private Map<String, Object> emailParams;


    //TODO
    // build and package a clozing-tag-utility-service
    // and make this file a visible for the two projects (device and notification services)
}