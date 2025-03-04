package com.clozingtag.clozingtag.device.service.repository;

import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    Page<DeviceEntity> findByBrandContainingIgnoreCase(String brand, Pageable pageable);
    Page<DeviceEntity> findByState(DeviceState state, Pageable pageable);
}