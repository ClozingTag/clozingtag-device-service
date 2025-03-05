package com.clozingtag.clozingtag.device.service.impl;


import com.clozingtag.clozingtag.device.service.custom.CustomExchangeService;
import com.clozingtag.clozingtag.device.service.custom.CustomUserEntity;
import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.dto.NotificationRequest;
import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import com.clozingtag.clozingtag.device.service.exception.DeviceNotFoundException;
import com.clozingtag.clozingtag.device.service.exception.DeviceStateException;
import com.clozingtag.clozingtag.device.service.mapper.DeviceMapper;
import com.clozingtag.clozingtag.device.service.repository.DeviceRepository;
import com.clozingtag.clozingtag.device.service.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    private final CustomExchangeService customExchangeService;
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(CustomExchangeService customExchangeService, DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.customExchangeService = customExchangeService;
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public DeviceResponse createDevice(DeviceRequest deviceRequest) {
        log.info("Creating a new device: {}", deviceRequest);
        DeviceEntity deviceEntity = deviceMapper.createDeviceEntityFromRequest(deviceRequest);
        log.info("Device mapped successfully: {}", deviceEntity);
        DeviceEntity savedDevice = deviceRepository.save(deviceEntity);
        log.info("Device created successfully with ID: {}", savedDevice.getId());
        sendDeviceLogs(savedDevice, "creation");
        return buildDeviceResponse(savedDevice);
    }

    @Override
    public DeviceResponse updateDevice(Long id, DeviceRequest updateRequest) {
        DeviceEntity deviceEntity = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        if (!EnumSet.allOf(DeviceState.class).contains(updateRequest.getState())) {
            throw new DeviceStateException("Invalid state value: " + updateRequest.getState() + ". Valid values are: " + EnumSet.allOf(DeviceState.class));
        }

        if (deviceEntity.getState() == DeviceState.IN_USE) {
            if (!updateRequest.getName().isEmpty() || !updateRequest.getBrand().isEmpty()) {
                throw new DeviceStateException("Cannot update name or brand when device is in use.");
            }
        }

        deviceMapper.updateDeviceEntityFromRequest(updateRequest, deviceEntity);
        DeviceEntity savedDeviceEntity = deviceRepository.save(deviceEntity);
        sendDeviceLogs(savedDeviceEntity, "update");
        return buildDeviceResponse(savedDeviceEntity);
    }

    @Override
    public DeviceResponse updateDevicePartially(Long id, DeviceState deviceState) {
        DeviceEntity deviceEntity = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        if (!EnumSet.allOf(DeviceState.class).contains(deviceState)) {
            throw new DeviceStateException("Invalid state value: " + deviceState + ". Valid values are: " + EnumSet.allOf(DeviceState.class));
        }

        deviceEntity.setState(deviceState);
        return buildDeviceResponse(deviceRepository.save(deviceEntity));
    }

    @Override
    public DeviceResponse getDeviceById(Long id) {
        log.info("Fetching device with ID: {}", id);
        return deviceRepository.findById(id)
                .map(this::buildDeviceResponse)
                .orElseThrow(() -> {
                    log.warn("Device with ID {} not found", id);
                    return new DeviceNotFoundException("Device not found with id: " + id);
                });
    }

    @Override
    public List<DeviceResponse> getAllDevices(Pageable pageable) {
        log.info("Fetching all devices with pagination: {}", pageable);
        return deviceRepository.findAll(pageable).getContent()
                .stream().map(this::buildDeviceResponse).toList();
    }

    @Override
    public void deleteDevice(Long id) {
        log.info("Deleting device with ID: {}", id);
        DeviceEntity device = deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Device with Id {} not found", id);
                    return new DeviceNotFoundException("Device not found with id: " + id);
                });

        if (device.getState() == DeviceState.IN_USE) {
            log.warn("Device with ID {} in use", id);
            throw new DeviceStateException("Cannot delete a device that is in use.");
        }
        sendDeviceLogs(device, "deletion");
        deviceRepository.delete(device);
    }


    @Override
    public List<DeviceResponse> getDevicesByBrand(String brand, Pageable pageable) {
        log.info("Fetching all devices by brand: {}", brand);
        return deviceRepository.findByBrandContainingIgnoreCase(brand, pageable).getContent().stream().map(this::buildDeviceResponse).toList();
    }

    @Override
    public List<DeviceResponse> getDevicesByState(DeviceState deviceState, Pageable pageable) {
        log.info("Fetching all devices by state: {}", deviceState);
        if (!EnumSet.allOf(DeviceState.class).contains(deviceState)) {
            throw new DeviceStateException("Invalid state value: " + deviceState + ". Valid values are: " + EnumSet.allOf(DeviceState.class));
        }
        return deviceRepository.findByState(deviceState, pageable).getContent().stream().map(this::buildDeviceResponse).toList();
    }

    private DeviceResponse buildDeviceResponse(DeviceEntity deviceEntity) {
        return deviceMapper.createDeviceResponseFromEntity(deviceEntity);
    }


    private void sendDeviceLogs(DeviceEntity device, String option) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy HH:mm:ss");
        CustomUserEntity customUserEntity = getAuthenticatedUser();
        String body = "Hi " + customUserEntity.getName() + ",\n\n"
                + "A device " + option + " occurred.\n"
                + "Name: " + device.getName() + "\n"
                + "Brand: " + device.getBrand() + "\n"
                + "Current State: " + device.getState().name() + "\n"
                + "Time: " + LocalDateTime.now().format(formatter) + "\n"
                + "Stay safe,";
        customExchangeService.createNotification(NotificationRequest.builder()
                .email(customUserEntity.getUsername())
                .body(body)
                .userId(customUserEntity.getId())
                .name(customUserEntity.getName())
                .userId(customUserEntity.getId())
                .template(option)
                .emailParams(Map.of("name", device.getName(), "brand", device.getBrand(), "state", device.getState().name(), "data",
                        LocalDateTime.now().format(formatter)))
                .subject(customUserEntity.getUsername())
                .build());


    }


    private CustomUserEntity getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserEntity) {
            return (CustomUserEntity) principal;
        } else if (principal instanceof UserDetails) {
            System.out.println("principal = " + principal);
//            return new CustomUserEntity((UserDetails) principal);
            return (CustomUserEntity) principal;
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }
}
