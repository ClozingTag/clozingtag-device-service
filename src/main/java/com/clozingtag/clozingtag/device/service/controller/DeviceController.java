package com.clozingtag.clozingtag.device.service.controller;


import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import com.clozingtag.clozingtag.device.service.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@Tag(name = "Devices", description = "Device Management API")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody DeviceRequest device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.createDevice(device));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable Long id, @RequestBody DeviceRequest device) {
        return ResponseEntity.ok(deviceService.updateDevice(id, device));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices(
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(deviceService.getAllDevices(PageRequest.of(page, size)));
    }


    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByBrand(@PathVariable String brand,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(deviceService.getDevicesByBrand(brand, PageRequest.of(page, size)));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByState(@PathVariable DeviceState state,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(deviceService.getDevicesByState(state, PageRequest.of(page, size)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}