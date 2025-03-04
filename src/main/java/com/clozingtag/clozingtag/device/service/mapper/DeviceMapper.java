package com.clozingtag.clozingtag.device.service.mapper;


import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceEntity createDeviceEntityFromRequest(DeviceRequest deviceRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeviceEntityFromRequest(DeviceRequest deviceRequest, @MappingTarget DeviceEntity device);

    DeviceResponse createDeviceResponseFromEntity(DeviceEntity device);
}