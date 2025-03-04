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

    @Operation(summary = "Create a new device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody DeviceRequest device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.createDevice(device));
    }

    @Operation(summary = "Update an existing device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input or device in use"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable Long id, @RequestBody DeviceRequest device) {
        return ResponseEntity.ok(deviceService.updateDevice(id, device));
    }

    @Operation(summary = "Get device by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @Operation(summary = "Get all devices")
    @ApiResponse(responseCode = "200", description = "List of devices")
    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices(
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(deviceService.getAllDevices(PageRequest.of(page, size)));
    }

    @Operation(summary = "Get devices by brand")
    @ApiResponse(responseCode = "200", description = "List of devices by brand")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByBrand(@PathVariable String brand,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(deviceService.getDevicesByBrand(brand, PageRequest.of(page, size)));
    }

    @Operation(summary = "Get devices by state")
    @ApiResponse(responseCode = "200", description = "List of devices by state")
    @GetMapping("/state/{state}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByState(@PathVariable DeviceState state,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
                                                                  @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(deviceService.getDevicesByState(state, PageRequest.of(page, size)));
    }

    @Operation(summary = "Delete a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device deleted"),
            @ApiResponse(responseCode = "400", description = "Device in use"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}