package com.clozingtag.clozingtag.device.service.dto;

import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DeviceResponse(
        Long id, String name, String brand, DeviceState state, LocalDateTime creationTime
) {
}
