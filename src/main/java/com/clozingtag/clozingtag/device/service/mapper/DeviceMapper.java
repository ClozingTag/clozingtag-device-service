package com.clozingtag.clozingtag.device.service.mapper;


import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceEntity createDeviceEntityFromRequest(DeviceRequest deviceRequest);

    void updateDeviceEntityFromRequest(DeviceRequest updateRequest, @MappingTarget DeviceEntity device);

    DeviceResponse createDeviceResponseFromEntity(DeviceEntity device);
}