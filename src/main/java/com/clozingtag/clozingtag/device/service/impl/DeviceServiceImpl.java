package com.clozingtag.clozingtag.device.service.impl;


import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import com.clozingtag.clozingtag.device.service.exception.DeviceNotFoundException;
import com.clozingtag.clozingtag.device.service.exception.DeviceStateException;
import com.clozingtag.clozingtag.device.service.mapper.DeviceMapper;
import com.clozingtag.clozingtag.device.service.repository.DeviceRepository;
import com.clozingtag.clozingtag.device.service.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public DeviceResponse createDevice(DeviceRequest device) {
        log.info("Creating a new device: {}", device);
        DeviceEntity deviceEntity = deviceMapper.createDeviceEntityFromRequest(device);
        log.info("Device mapped successfully: {}", deviceEntity);
        DeviceEntity savedDevice = deviceRepository.save(deviceEntity);
        log.info("Device created successfully with ID: {}", savedDevice.getId());
        return buildDeviceResponse(savedDevice);
    }

    @Override
    public DeviceResponse updateDevice(Long id, DeviceRequest deviceRequest) {
        DeviceEntity deviceEntity = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        if (!EnumSet.allOf(DeviceState.class).contains(deviceRequest.getState())) {
            throw new DeviceStateException("Invalid state value: " + deviceRequest.getState() + ". Valid values are: " + EnumSet.allOf(DeviceState.class));
        }

        if (deviceEntity.getState() == DeviceState.IN_USE) {
            if (deviceRequest.getName() != null || deviceRequest.getBrand() != null) {
                throw new DeviceStateException("Cannot update name or brand when device is in use.");
            }
        }

        deviceMapper.updateDeviceEntityFromRequest(deviceRequest, deviceEntity);
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
}
