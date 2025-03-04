package com.clozingtag.clozingtag.device.service.service;


import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeviceService {
     DeviceResponse createDevice(DeviceRequest device);

     DeviceResponse updateDevice(Long id, DeviceRequest device);

     DeviceResponse getDeviceById(Long id);

     List<DeviceResponse> getAllDevices(Pageable pageable);

     void deleteDevice(Long id);

     List<DeviceResponse> getDevicesByBrand(String brand, Pageable pageable);

     List<DeviceResponse> getDevicesByState(DeviceState deviceState, Pageable pageable);
}
