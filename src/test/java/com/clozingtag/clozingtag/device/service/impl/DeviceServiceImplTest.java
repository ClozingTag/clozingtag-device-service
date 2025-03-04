package com.clozingtag.clozingtag.device.service.impl;

import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import com.clozingtag.clozingtag.device.service.exception.DeviceNotFoundException;
import com.clozingtag.clozingtag.device.service.exception.DeviceStateException;
import com.clozingtag.clozingtag.device.service.mapper.DeviceMapper;
import com.clozingtag.clozingtag.device.service.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    private DeviceEntity deviceEntity;

    private DeviceResponse deviceResponse;

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        deviceEntity = DeviceEntity.builder()
                .id(1L)
                .name("Test Device")
                .brand("Test Brand")
                .state(DeviceState.AVAILABLE)
                .creationTime(LocalDateTime.now())
                .build();

        deviceResponse = DeviceResponse.builder()
                .id(1L)
                .name("Test Device")
                .brand("Test Brand")
                .state(DeviceState.AVAILABLE)
                .creationTime(LocalDateTime.now())
                .build();


        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void createDevice_success() {
        DeviceRequest deviceRequest = DeviceRequest.builder()
                .name("Test Device")
                .brand("Test Brand")
                .state(DeviceState.AVAILABLE)
                .build();

        when(deviceMapper.createDeviceEntityFromRequest(deviceRequest)).thenReturn(deviceEntity);
        when(deviceRepository.save(any(DeviceEntity.class))).thenReturn(deviceEntity);
        when(deviceMapper.createDeviceResponseFromEntity(deviceEntity)).thenReturn(deviceResponse);

        DeviceResponse createdDevice = deviceService.createDevice(deviceRequest);

        assertNotNull(createdDevice);
        assertEquals(deviceResponse.name(), createdDevice.name());
        verify(deviceRepository, times(1)).save(any(DeviceEntity.class));
    }

    @Test
    void getDeviceById_success() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity));
        when(deviceMapper.createDeviceResponseFromEntity(deviceEntity)).thenReturn(deviceResponse);
        DeviceResponse retrievedDevice = deviceService.getDeviceById(1L);

        assertNotNull(retrievedDevice);
        assertEquals("Test Device", retrievedDevice.name());
    }

    @Test
    void getDeviceById_notFound() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(2L));
    }

    @Test
    void getAllDevices_success() {
        when(deviceRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(deviceEntity), pageRequest, 1));
        when(deviceMapper.createDeviceResponseFromEntity(deviceEntity)).thenReturn(deviceResponse);
        List<DeviceResponse> devices = deviceService.getAllDevices(pageRequest);

        assertNotNull(devices);
        assertEquals(1, devices.size());
    }

    @Test
    void getDevicesByBrand_success() {
        when(deviceRepository.findByBrandContainingIgnoreCase("Test Brand", pageRequest))
                .thenReturn(new PageImpl<>(List.of(deviceEntity), pageRequest, 1));
        when(deviceMapper.createDeviceResponseFromEntity(deviceEntity)).thenReturn(deviceResponse);
        List<DeviceResponse> devices = deviceService.getDevicesByBrand("Test Brand", pageRequest);

        assertNotNull(devices);
        assertEquals(1, devices.size());
        assertEquals("Test Brand", devices.get(0).brand());
    }

    @Test
    void getDevicesByState_success() {
        when(deviceRepository.findByState(DeviceState.AVAILABLE, pageRequest))
                .thenReturn(new PageImpl<>(List.of(deviceEntity), pageRequest, List.of(deviceEntity).size()));

        when(deviceMapper.createDeviceResponseFromEntity(deviceEntity)).thenReturn(deviceResponse);
        List<DeviceResponse> devices = deviceService.getDevicesByState(DeviceState.AVAILABLE, pageRequest);

        assertNotNull(devices);
        assertEquals(1, devices.size());
        assertEquals(DeviceState.AVAILABLE, devices.getFirst().state());
    }

    @Test
    void updateDevice_success() {
        DeviceRequest updateDeviceRequest = DeviceRequest.builder()
                .name("Updated Device")
                .brand("Updated Brand")
                .state(DeviceState.IN_USE)
                .build();


        DeviceEntity updatedDeviceEntity = DeviceEntity.builder()
                .id(1L)
                .name("Updated Device")
                .brand("Updated Brand")
                .state(DeviceState.IN_USE)
                .build();

        deviceResponse =  DeviceResponse.builder()
                .id(1L)
                .name("Updated Device")
                .brand("Updated Brand")
                .state(DeviceState.IN_USE)
                .creationTime(LocalDateTime.now())
                .build();


        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity));
        doNothing().when(deviceMapper).updateDeviceEntityFromRequest(updateDeviceRequest, deviceEntity);
        when(deviceRepository.save(any(DeviceEntity.class))).thenReturn(updatedDeviceEntity);
        when(deviceMapper.createDeviceResponseFromEntity(updatedDeviceEntity)).thenReturn(deviceResponse);

        DeviceResponse updatedDevice = deviceService.updateDevice(1L, updateDeviceRequest);

        assertNotNull(updatedDevice);
        assertEquals("Updated Device", updatedDevice.name());
        assertEquals("Updated Brand", updatedDevice.brand());
        assertEquals(DeviceState.IN_USE, updatedDevice.state());
    }


    @Test
    void updateDevice_deviceNotFound() {
        DeviceRequest updateDeviceRequest = DeviceRequest.builder()
                .name("Updated Device")
                .brand("Updated Brand")
                .state(DeviceState.IN_USE)
                .build();
        when(deviceRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(3L, updateDeviceRequest));
    }

    @Test
    void updateDevice_deviceInUse() {
        DeviceRequest updateDeviceRequest = DeviceRequest.builder()
                .name("Updated Device")
                .brand("Updated Brand")
                .state(DeviceState.IN_USE)
                .build();
        deviceEntity.setState(DeviceState.IN_USE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity));

        assertThrows(DeviceStateException.class, () -> deviceService.updateDevice(1L, updateDeviceRequest));
    }

    @Test
    void deleteDevice_success() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity));

        deviceService.deleteDevice(1L);

        verify(deviceRepository, times(1)).delete(deviceEntity);
    }

    @Test
    void deleteDevice_deviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DeviceNotFoundException.class, () -> deviceService.deleteDevice(1L));
    }

    @Test
    void deleteDevice_deviceInUse() {
        deviceEntity.setState(DeviceState.IN_USE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity));
        assertThrows(DeviceStateException.class, () -> deviceService.deleteDevice(1L));
    }
}